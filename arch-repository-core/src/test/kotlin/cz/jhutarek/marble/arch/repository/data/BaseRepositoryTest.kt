package cz.jhutarek.marble.arch.repository.data

import com.nhaarman.mockitokotlin2.*
import cz.jhutarek.marble.arch.repository.data.BaseRepositoryTest.MockRepositoryBuilder.SourceResult.*
import cz.jhutarek.marble.arch.repository.data.BaseRepositoryTest.MockRepositoryBuilder.SourceResult.Any
import cz.jhutarek.marble.arch.repository.model.Data
import io.reactivex.Maybe
import io.reactivex.MaybeObserver
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource

internal class BaseRepositoryTest {

    internal class MockRepositoryBuilder(allResults: List<SourceResult> = listOf(Any())) {
        internal sealed class SourceResult {
            abstract val maybeSpy: Maybe<String>

            class Any : SourceResult() {
                override val maybeSpy: Maybe<String> = spy(Maybe.empty())
                override fun toString() = "any"
            }

            class Empty : SourceResult() {
                override val maybeSpy: Maybe<String> = spy(Maybe.empty())
                override fun toString() = "empty"
            }

            data class Value(private val value: String = "anyValue") : SourceResult() {
                override val maybeSpy: Maybe<String> = spy(Maybe.just(value))
                override fun toString() = value
            }

            data class Error(private val error: Throwable = IllegalStateException("anyError")) : SourceResult() {
                override val maybeSpy: Maybe<String> = spy(Maybe.error(error))
                override fun toString() = "$error"
            }
        }

        private val sourceResult = allResults.last()
        private val cacheResults = allResults.dropLast(1)

        val sourceMock = mock<Source<String>> { on { request() } doReturn sourceResult.maybeSpy }
        val sourceResultSpy = sourceResult.maybeSpy

        val cacheMocks = cacheResults.map { result -> mock<Cache<String>> { on { request() } doReturn result.maybeSpy } }
        val cacheResultSpies = cacheResults.map { it.maybeSpy }

        val allMocks = cacheMocks + sourceMock
        val allResultSpies = cacheResultSpies + sourceResultSpy

        val repository = object : BaseRepository<String>(sourceMock, *cacheMocks.toTypedArray()) {}
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 2, 3, 10])
    fun `repository should request each source when requested`(sourceCount: Int) {
        MockRepositoryBuilder(List(sourceCount) { Any() }).run {
            repository.request()

            assertThat(allMocks).allSatisfy { verify(it).request() }
        }
    }

    @ParameterizedTest
    @MethodSource("subscribeToSourcesUntilFirstValueData")
    fun `repository should subscribe to sources in order until the first of them returns value`(expectedSubscribedSources: Int, allResults: List<MockRepositoryBuilder.SourceResult>) {
        MockRepositoryBuilder(allResults).run {
            repository.request()

            assertThat(allResultSpies.take(expectedSubscribedSources)).allSatisfy { verify(it).subscribe(any<MaybeObserver<String>>()) }
            assertThat(allResultSpies.drop(expectedSubscribedSources)).allSatisfy { verifyZeroInteractions(it) }
        }
    }

    @Test
    fun `repository should emit loading first when requested`() {
        MockRepositoryBuilder().run {
            val testObserver = repository.observe().test()

            repository.request()

            testObserver.assertValue(Data.Loading)
        }
    }

    companion object {
        @JvmStatic
        fun subscribeToSourcesUntilFirstValueData() = arrayOf(
                arguments(1, listOf(Empty())),
                arguments(1, listOf(Value())),
                arguments(1, listOf(Error())),
                arguments(1, listOf(Value(), Empty())),
                arguments(1, listOf(Value(), Value())),
                arguments(1, listOf(Value(), Value(), Value(), Empty(), Error(), Value())),
                arguments(2, listOf(Empty(), Empty())),
                arguments(2, listOf(Empty(), Value())),
                arguments(2, listOf(Empty(), Value(), Value(), Value())),
                arguments(5, listOf(Empty(), Empty(), Empty(), Empty(), Value(), Value(), Error()))
        )
    }

    /*@ParameterizedTest
    @MethodSource("subscribeUntilFirstValueData")
    fun `repository should subscribe to sources in order until the first of them returns value`(expectedSubscriptions: Int, caches: List<Pair<Cache<String>, Maybe<String>>>, source: Pair<Source<String>, Maybe<String>>) {
        val allSources = listOf(source) + caches
        val repository = Repository(source.first, *caches.map { it.first }.toTypedArray())

        repository.request()

        assertThat(allSources.take(expectedSubscriptions)).allSatisfy { verify(it.second).subscribe(any<MaybeObserver<String>>()) }
        assertThat(allSources.drop(expectedSubscriptions)).allSatisfy { verifyZeroInteractions(it.second) }
    }*/

/*
    private class Repository(source: Source<String>, vararg caches: Cache<String>) : BaseRepository<String>(source, *caches)

    private val anyData = "anyData"
    private val anySource = mock<Source<String>>()

    private lateinit var repository: Repository
    private lateinit var testObserver: TestObserver<Data<String>>

    @BeforeEach
    fun init() {
        reset(anySource)
    }

    @Nested
    @TestInstance(PER_CLASS)
    @DisplayName("given no caches are injected, ")
    internal inner class NoCachesInjected {
        private val anyException = IllegalStateException("anyException")

        @BeforeEach
        fun init() {
            repository = Repository(anySource)
            testObserver = repository.observe().test()
        }

        @Test
        fun `when observing value from repository, source should be requested and subscribed to`() {
            val anySourceMaybe = spy(Maybe.just(anyData))
            whenever(anySource.request()).thenReturn(anySourceMaybe)

            repository.request()

            verify(anySource).request()
            verify(anySourceMaybe).subscribe(any<MaybeObserver<String>>())
        }

        @Test
        fun `when source emits value, repository should emit loading and then value`() {
            assertRepositoryEmitsLoadingAndThen(Data.Loaded(anyData), Maybe.just(anyData))
        }

        @Test
        fun `when source is empty, repository should emit loading and then empty`() {
            assertRepositoryEmitsLoadingAndThen(Data.Empty, Maybe.empty())
        }

        @Test
        fun `when source emits error, repository should emit loading and then error`() {
            assertRepositoryEmitsLoadingAndThen(Data.Error(anyException), Maybe.error(anyException))
        }

        private fun assertRepositoryEmitsLoadingAndThen(data: Data<String>, sourceRequestResult: Maybe<String>) {
            whenever(anySource.request()).thenReturn(sourceRequestResult)

            repository.request()

            testObserver.assertValues(Data.Loading, data)
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    @DisplayName("given multiple caches are injected, ")
    internal inner class MultipleCachesInjected {
        private val anyCache1 = mock<Cache<String>>()
        private val anyCache2 = mock<Cache<String>>()

        @BeforeEach
        fun init() {
            reset(anyCache1, anyCache2)
            repository = Repository(anySource, anyCache1, anyCache2)
            testObserver = repository.observe().test()
        }

        @Test
        fun `when observing value from repository and only first cache emits value, other sources should be requested, but not subscribed to`() {
            val anyCache1Maybe = spy(Maybe.just(anyData))
            val anyCache2Maybe = spy(Maybe.empty<String>())
            val anySourceMaybe = spy(Maybe.empty<String>())
            whenever(anyCache1.request()).thenReturn(anyCache1Maybe)
            whenever(anyCache2.request()).thenReturn(anyCache2Maybe)
            whenever(anySource.request()).thenReturn(anySourceMaybe)

            repository.request()

            verify(anyCache1).request()
            verify(anyCache2).request()
            verify(anySource).request()
            verify(anyCache1Maybe).subscribe(any<MaybeObserver<String>>())
            verifyZeroInteractions(anyCache2Maybe, anySourceMaybe)
        }

        @Test
        fun `when first cache emits value, repository should emit loading and then value`() {
            whenever(anyCache1.request()).thenReturn(Maybe.just(anyData))

            repository.request()

            testObserver.assertValues(Data.Loading, Data.Loaded(anyData))
        }
    }*/
}