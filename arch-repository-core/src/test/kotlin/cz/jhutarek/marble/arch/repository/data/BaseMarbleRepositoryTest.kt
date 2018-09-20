package cz.jhutarek.marble.arch.repository.data

import io.reactivex.Completable
import io.reactivex.Maybe
import org.junit.jupiter.api.Test

internal class BaseMarbleRepositoryTest {

    private class Cache(private val maybe: Maybe<String>, private val tag: String) : MarbleCache<String> {
        override fun store(data: String): Completable = Completable.fromCallable { println("Stored $data in $this") }.doOnSubscribe { println("Subscribed to store in $this") }

        override fun clear(): Completable {
            TODO()
        }

        override fun load(): Maybe<String> = maybe.doOnSubscribe { println("Subscribed to load in $this") }

        override fun toString() = "cache $tag"
    }

    private class Source(private val maybe: Maybe<String>) : MarbleSource<String> {
        override fun load(): Maybe<String> = maybe.doOnSubscribe { println("Subscribed to load in source") }
    }

    @Test
    fun tmp() {
        val cache1 = Cache(Maybe.empty<String>(), "1")
        val cache2 = Cache(Maybe.just("11111"), "2")
        val cache3 = Cache(Maybe.error(IllegalAccessException()), "3")
        val cache4 = Cache(Maybe.just("XXX"), "4")
        val source = Source(Maybe.just("aaa"))
        val sources = arrayOf(cache1, cache2, cache3, cache4, source)

        fun storeInHigherCaches(sourceIndex: Int, value: String): Maybe<String> {
            return Completable.concat(
                    sources
                            .take(sourceIndex)
                            .filterIsInstance<MarbleCache<String>>()
                            .map { it.store(value) }
            )
                    .toSingle { value }
                    .toMaybe()
        }

        Maybe.concat(sources.mapIndexed { i, it -> it.load().map { i to it } })
                .firstElement()
                .flatMap { storeInHigherCaches(it.first, it.second) }
                .subscribe({ it ->
                    println("Success: $it")
                }, {
                    println("Error: $it")
                }, {
                    println("Complete") // TODO map errors and completions to data
                })
    }
}