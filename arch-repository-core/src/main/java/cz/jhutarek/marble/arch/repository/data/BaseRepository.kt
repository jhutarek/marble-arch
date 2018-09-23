package cz.jhutarek.marble.arch.repository.data

import com.jakewharton.rxrelay2.PublishRelay
import cz.jhutarek.marble.arch.repository.domain.Repository
import cz.jhutarek.marble.arch.repository.model.Data
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable

abstract class BaseRepository<K : Any, D : Any>(
        source: Source<K, D>,
        vararg caches: Cache<K, D>
) : Repository<K, D> {

    private data class IndexedResult<out D : Any>(val index: Int, val value: D)

    private val allSources = caches.toList() + source
    private val relay = PublishRelay.create<Data<K, D>>()

    final override fun observe(): Observable<Data<K, D>> = relay.hide()

    final override fun request(key: K) {
        allSources
                .mapIndexed { index, source ->
                    source.request(key)
                            .map { result -> IndexedResult(index, result) }
                }
                .let { Maybe.concat(it) }
                .firstElement()
                .flatMap { storeValueInCaches(key, it) }
                .map { Data.Loaded(key, it) as Data<K, D> }
                .toSingle(Data.Empty(key))
                .toObservable()
                .startWith(Data.Loading(key))
                .onErrorReturn { Data.Error(key, it) }
                .subscribe(relay)
    }

    private fun storeValueInCaches(key: K, indexedResult: IndexedResult<D>): Maybe<D> = Completable.merge(
            allSources
                    .take(indexedResult.index)
                    .filterIsInstance<Cache<K, D>>()
                    .map { it.store(key, indexedResult.value) }
    )
            .toSingle { indexedResult.value }
            .toMaybe()
}