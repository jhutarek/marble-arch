package cz.jhutarek.marble.arch.repository.data

import com.jakewharton.rxrelay2.PublishRelay
import cz.jhutarek.marble.arch.repository.domain.Repository
import cz.jhutarek.marble.arch.repository.model.Data
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class BaseRepository<Q : Any, D : Any>(
    source: Source<Q, D>,
    private vararg val caches: Cache<Q, D>
) : Repository<Q, D> {

    private data class IndexedResult<out D : Any>(val index: Int, val value: D)

    private val allSources = caches.toList() + source
    private val relay = PublishRelay.create<Data<Q, D>>()

    final override fun observe(): Observable<Data<Q, D>> = relay.hide().distinctUntilChanged()

    final override fun load(query: Q) {
        allSources
            .mapIndexed { index, source ->
                source.request(query)
                    .map { result -> IndexedResult(index, result) }
            }
            .let { Maybe.concat(it) }
            .firstElement()
            .flatMap { storeValueInCaches(query, it) }
            .map { Data.Loaded(query, it) as Data<Q, D> }
            .toSingle(Data.Empty(query))
            .toObservable()
            .startWith(Data.Loading(query))
            .onErrorReturn { Data.Error(query, it) }
            .subscribe(relay)
    }

    final override fun update(query: Q) {
        Completable.concat(
            listOf(
                clearCaches(query),
                Completable.fromAction { load(query) }
            )
        )
            .toObservable<Data<Q, D>>()
            .startWith(Data.Loading(query))
            .onErrorReturn { Data.Error(query, it) }
            .subscribe(relay)
    }

    final override fun clearCaches(query: Q?): Completable {
        val resultSubject = PublishSubject.create<Unit>()

        Completable.merge(caches.map { it.clear(query) }).toObservable<Unit>().subscribe(resultSubject)

        return resultSubject.hide().ignoreElements()
    }

    private fun storeValueInCaches(query: Q, indexedResult: IndexedResult<D>): Maybe<D> = Completable.merge(
        allSources
            .take(indexedResult.index)
            .filterIsInstance<Cache<Q, D>>()
            .map { it.store(query, indexedResult.value) }
    )
        .toSingle { indexedResult.value }
        .toMaybe()
}