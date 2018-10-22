package cz.jhutarek.marble.arch.repository.data

import cz.jhutarek.marble.arch.repository.data.BaseRepositoryTest.MockRepositoryBuilder.SourceResult.*
import cz.jhutarek.marble.arch.repository.data.BaseRepositoryTest.MockRepositoryBuilder.SourceResult.Error.Companion.EXPECTED_ERROR
import cz.jhutarek.marble.arch.repository.data.BaseRepositoryTest.MockRepositoryBuilder.SourceResult.Value.Companion.EXPECTED_VALUE
import cz.jhutarek.marble.arch.repository.model.Data
import cz.jhutarek.marble.arch.test.infrastructure.InstancePerClassStringSpec
import io.kotlintest.data.forall
import io.kotlintest.inspectors.forAll
import io.kotlintest.tables.row
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Maybe
import io.reactivex.MaybeObserver

internal class BaseRepositoryTest : InstancePerClassStringSpec() {
    internal class MockRepositoryBuilder(allResults: List<SourceResult> = listOf(SourceResult.Whatever())) {
        val query = "query"

        internal sealed class SourceResult {
            abstract val maybeSpy: Maybe<String>

            class Whatever : SourceResult() {
                override val maybeSpy: Maybe<String> = spyk(Maybe.empty())
                override fun toString() = "any"
            }

            class Empty : SourceResult() {
                override val maybeSpy: Maybe<String> = spyk(Maybe.empty())
                override fun toString() = "empty"
            }

            data class Value(private val value: String = ANY_VALUE) : SourceResult() {
                companion object {
                    const val ANY_VALUE = "any value"
                    const val EXPECTED_VALUE = "expected value"
                }

                override val maybeSpy: Maybe<String> = spyk(Maybe.just(value))
                override fun toString() = value
            }

            data class Error(private val error: Throwable = ANY_ERROR) : SourceResult() {
                companion object {
                    val ANY_ERROR = IllegalStateException("any error")
                    val EXPECTED_ERROR = IllegalStateException("expected error")
                }

                override val maybeSpy: Maybe<String> = spyk(Maybe.error(error))
                override fun toString() = "$error"
            }
        }

        private val sourceResult = allResults.last()
        private val cacheResults = allResults.dropLast(1)

        private val sourceMock = mockk<Source<String, String>> {
            every { request(query) } returns sourceResult.maybeSpy
        }
        private val sourceResultSpy = sourceResult.maybeSpy

        val cacheClearSpies = List(cacheResults.size) { spyk(Completable.complete()) }
        val cacheMocks = cacheResults.mapIndexed { index, result ->
            mockk<Cache<String, String>> {
                every { request(query) } returns result.maybeSpy
                every { store(eq(query), any()) } returns Completable.complete()
                every { clear(query) } returns cacheClearSpies[index]
            }
        }
        private val cacheResultSpies = cacheResults.map { it.maybeSpy }

        val allMocks = cacheMocks + sourceMock
        val allResultSpies = cacheResultSpies + sourceResultSpy

        val repository = spyk(object : BaseRepository<String, String>(sourceMock, *cacheMocks.toTypedArray()) {})
    }

    init {
        "repository should request each source when loaded" {
            forall(
                row(1),
                row(2),
                row(3),
                row(10)
            ) { sourceCount ->
                BaseRepositoryTest.MockRepositoryBuilder(List(sourceCount) { BaseRepositoryTest.MockRepositoryBuilder.SourceResult.Whatever() })
                    .run {
                        clearMocks(*allMocks.toTypedArray(), answers = false)

                        repository.load(query)

                        allMocks.forAll { verify { it.request(query) } }
                    }
            }
        }

        "repository should subscribe to sources until the first of them returns value" {
            forall(
                row(1, listOf(Empty())),
                row(1, listOf(Value())),
                row(1, listOf(Error())),
                row(1, listOf(Value(), Empty())),
                row(1, listOf(Value(), Value())),
                row(1, listOf(Value(), Value(), Value(), Empty(), Error(), Value())),
                row(2, listOf(Empty(), Empty())),
                row(2, listOf(Empty(), Value())),
                row(2, listOf(Empty(), Value(), Value(), Value())),
                row(5, listOf(Empty(), Empty(), Empty(), Empty(), Value(), Value(), Error()))
            ) { expectedSubscribedSources, allResults ->
                MockRepositoryBuilder(allResults).run {
                    clearMocks(*allResultSpies.toTypedArray(), answers = false)

                    repository.load(query)

                    allResultSpies.take(expectedSubscribedSources).forAll {
                        verify { it.subscribe(any<MaybeObserver<String>>()) }
                    }
                    allResultSpies.drop(expectedSubscribedSources).forAll {
                        verify(inverse = true) { it.subscribe(any<MaybeObserver<String>>()) }
                    }
                }
            }
        }

        "repository should emit loading first when loading" {
            MockRepositoryBuilder().run {
                val testObserver = repository.observe().test()

                repository.load(query)

                testObserver.assertValueAt(0, Data.Loading(query))
            }
        }

        "repository should emit value from first source that is not empty or error" {
            forall(
                row(listOf(Value(EXPECTED_VALUE))),
                row(listOf(Value(EXPECTED_VALUE), Empty())),
                row(listOf(Value(EXPECTED_VALUE), Value())),
                row(listOf(Value(EXPECTED_VALUE), Value(), Value(), Empty(), Error(), Value())),
                row(listOf(Empty(), Value(EXPECTED_VALUE))),
                row(listOf(Empty(), Value(EXPECTED_VALUE), Value(), Value())),
                row(listOf(Empty(), Empty(), Empty(), Empty(), Value(EXPECTED_VALUE), Value(), Error()))
            ) { allResults ->
                MockRepositoryBuilder(allResults).run {
                    val testObserver = repository.observe().test()

                    repository.load(query)

                    testObserver.assertValueAt(1, Data.Loaded(query, EXPECTED_VALUE))
                }
            }
        }

        "repository should emit error from first source that is not empty or has value" {
            forall(
                row(listOf(Error(EXPECTED_ERROR))),
                row(listOf(Error(EXPECTED_ERROR), Empty())),
                row(listOf(Error(EXPECTED_ERROR), Error())),
                row(listOf(Error(EXPECTED_ERROR), Error(), Value(), Empty(), Error(), Value())),
                row(listOf(Empty(), Error(EXPECTED_ERROR))),
                row(listOf(Empty(), Error(EXPECTED_ERROR), Error(), Value())),
                row(listOf(Empty(), Empty(), Empty(), Empty(), Error(EXPECTED_ERROR), Value(), Error()))
            ) { allResults ->
                MockRepositoryBuilder(allResults).run {
                    val testObserver = repository.observe().test()

                    repository.load(query)

                    testObserver.assertValueAt(1, Data.Error(query, EXPECTED_ERROR))
                }
            }
        }

        "repository should emit empty if all sources are empty" {
            forall(
                row(1),
                row(2),
                row(3),
                row(10)
            ) { sourceCount ->
                MockRepositoryBuilder(List(sourceCount) { Empty() }).run {
                    val testObserver = repository.observe().test()

                    repository.load(query)

                    testObserver.assertValueAt(1, Data.Empty(query))
                }
            }
        }

        "repository should store value loaded from lower source in all higher caches" {
            forall(
                row(0, listOf(Empty())),
                row(0, listOf(Value())),
                row(0, listOf(Error())),
                row(0, listOf(Value(), Empty())),
                row(0, listOf(Value(), Value())),
                row(0, listOf(Empty(), Empty())),
                row(1, listOf(Empty(), Value(EXPECTED_VALUE))),
                row(2, listOf(Empty(), Empty(), Value(EXPECTED_VALUE), Empty(), Error(), Value())),
                row(4, listOf(Empty(), Empty(), Empty(), Empty(), Value(EXPECTED_VALUE), Value(), Error()))
            ) { expectedStoreCount, allResults ->
                MockRepositoryBuilder(allResults).run {
                    clearMocks(*cacheMocks.toTypedArray(), answers = false)

                    repository.load(query)

                    cacheMocks.take(expectedStoreCount).forAll { verify { it.store(query, EXPECTED_VALUE) } }
                    cacheMocks.drop(expectedStoreCount).forAll { verify(inverse = true) { it.store(query, any()) } }
                }
            }
        }

        "repository should emit error if cache emits error when storing value" {
            MockRepositoryBuilder(listOf(Empty(), Value())).run {
                every { cacheMocks[0].store(query, any()) } returns Completable.error(EXPECTED_ERROR)
                val testObserver = repository.observe().test()

                repository.load(query)

                testObserver.assertValueAt(1, Data.Error(query, EXPECTED_ERROR))
            }
        }

        "repository should subscribe to clear all caches even if result completable is not subscribed" {
            MockRepositoryBuilder(listOf(Value(), Empty(), Empty())).run {
                repository.clearCaches(query)

                cacheClearSpies.forAll { verify { it.subscribe(any<CompletableObserver>()) } }
            }
        }

        "repository should emit complete when all caches are successfully cleared" {
            MockRepositoryBuilder(listOf(Value(), Empty(), Empty())).run {
                repository.clearCaches(query)
                    .test()
                    .assertComplete()
            }
        }

        "repository should emit error when any cache is not successfully cleared" {
            MockRepositoryBuilder(listOf(Value(), Empty(), Empty())).run {
                every { cacheMocks[1].clear(query) } returns Completable.error(EXPECTED_ERROR)

                repository.clearCaches(query)
                    .test()
                    .assertError(EXPECTED_ERROR)
            }
        }

        "repository should emit loading on update" {
            MockRepositoryBuilder().run {
                val testObserver = repository.observe().test()

                repository.update(query)

                testObserver.assertValueAt(0, Data.Loading(query))
            }
        }

        "repository should clear all caches and load on update" {
            MockRepositoryBuilder().run {
                repository.update(query)

                verifyOrder {
                    repository.clearCaches(query)
                    repository.load(query)
                }
            }
        }

        "repository should emit error if caches are not successfully cleared on update" {
            MockRepositoryBuilder(listOf(Value(), Empty(), Empty())).run {
                every { cacheMocks[1].clear(query) } returns Completable.error(EXPECTED_ERROR)
                val testObserver = repository.observe().test()

                repository.update(query)

                testObserver.assertValueAt(1, Data.Error(query, EXPECTED_ERROR))
            }
        }
    }
}