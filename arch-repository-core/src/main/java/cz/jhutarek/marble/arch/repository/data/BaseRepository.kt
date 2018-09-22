package cz.jhutarek.marble.arch.repository.data

import com.jakewharton.rxrelay2.PublishRelay
import cz.jhutarek.marble.arch.repository.domain.Repository
import cz.jhutarek.marble.arch.repository.model.Data
import io.reactivex.Maybe
import io.reactivex.Observable

// TODO support different keys
// TODO test
abstract class BaseRepository<D : Any>(
        source: Source<D>,
        vararg caches: Cache<D>
) : Repository<D> {

    private val allSources = caches.toList() + source

    private val relay = PublishRelay.create<Data<D>>()

    final override fun observe(): Observable<Data<D>> = relay.hide()

    final override fun request() {
        allSources.map { it.request() }
                .let { Maybe.concat(it) }
                .firstElement()
                .map { Data.Loaded(it) as Data<D> }
                .toObservable()
                .startWith(Data.Loading)
                .subscribe(relay)
    }
}

/*private class Cache(private val maybe: Maybe<String>, private val tag: String) : cz.jhutarek.marble.arch.repository.data.Cache<String> {
        override fun store(data: String): Completable = Completable.fromCallable { println("Stored $data in $this") }.doOnSubscribe { println("Subscribed to store in $this") }

        override fun clear(): Completable {
            TODO()
        }

        override fun request(): Maybe<String> = maybe.doOnSubscribe { println("Subscribed to request in $this") }

        override fun toString() = "cache $tag"
    }

    private class Source(private val maybe: Maybe<String>) : cz.jhutarek.marble.arch.repository.data.Source<String> {
        override fun request(): Maybe<String> = maybe.doOnSubscribe { println("Subscribed to request in source") }
    }

    @Test
    fun tmp() {
        val cache1 = Cache(Maybe.empty<String>(), "1")
        val cache2 = Cache(Maybe.empty<String>(), "2")
        val cache3 = Cache(Maybe.just("AAA"), "3")
        val cache4 = Cache(Maybe.empty<String>(), "4")
        val source = Source(Maybe.empty<String>())
        val sources = arrayOf(cache1, cache2, cache3, cache4, source)

        fun storeInHigherCaches(sourceIndex: Int, value: String): Maybe<String> {
            return Completable.concat(
                    sources
                            .take(sourceIndex)
                            .filterIsInstance<cz.jhutarek.marble.arch.repository.data.Cache<String>>()
                            .map { it.store(value) }
            )
                    .toSingle { value }
                    .toMaybe()
        }

        Maybe.concat(sources.mapIndexed { i, it -> it.request().map { i to it } })
                .firstElement()
                .flatMap { storeInHigherCaches(it.first, it.second) }
                .map { Data.Loaded(it) }
                .cast(Data::class.java)
                .onErrorReturn { Data.Error(it) }
                .toSingle(Data.Empty)
                .toObservable()
                .startWith(Data.Loading)
                .subscribe { it -> println("=> $it") }
    }*/