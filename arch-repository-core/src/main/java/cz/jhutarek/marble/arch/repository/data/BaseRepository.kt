package cz.jhutarek.marble.arch.repository.data

import com.jakewharton.rxrelay2.PublishRelay
import cz.jhutarek.marble.arch.repository.domain.Repository
import cz.jhutarek.marble.arch.repository.model.Data
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable

// TODO support different keys
abstract class BaseRepository<D : Any>(
        source: Source<D>,
        vararg caches: Cache<D>
) : Repository<D> {

    private data class IndexedResult<out D : Any>(val index: Int, val value: D)

    private val allSources = caches.toList() + source
    private val relay = PublishRelay.create<Data<D>>()

    final override fun observe(): Observable<Data<D>> = relay.hide()

    final override fun request() {
        allSources
                .mapIndexed { index, source ->
                    source.request()
                            .map { result -> IndexedResult(index, result) }
                }
                .let { Maybe.concat(it) }
                .firstElement()
                .flatMap { storeValueInCaches(it) }
                .map { Data.Loaded(it) as Data<D> }
                .toSingle(Data.Empty)
                .toObservable()
                .startWith(Data.Loading)
                .onErrorReturn { Data.Error(it) }
                .subscribe(relay)
    }

    private fun storeValueInCaches(indexedResult: IndexedResult<D>): Maybe<D> = Completable.merge(
            allSources
                    .take(indexedResult.index)
                    .filterIsInstance<Cache<D>>()
                    .map { it.store(indexedResult.value) }
    )
            .toSingle { indexedResult.value }
            .toMaybe()
}