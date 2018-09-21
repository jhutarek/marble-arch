package cz.jhutarek.marble.arch.repository.data

import com.nhaarman.mockitokotlin2.*
import cz.jhutarek.marble.arch.repository.data.BaseRepositoryTest.SourceResult.*
import io.reactivex.Maybe
import io.reactivex.MaybeObserver
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource

internal class BaseRepositoryTest {

    private class Repository(source: Source<String>, vararg caches: Cache<String>) : BaseRepository<String>(source, *caches)

    internal enum class SourceResult(val createMaybeSpy: () -> Maybe<String>) {
        EMPTY({ spy(Maybe.empty()) }),
        VALUE({ spy(Maybe.just("anyValue")) }),
        ERROR({ spy(Maybe.error(IllegalStateException("anyException"))) })
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 2, 3, 10])
    fun `repository should request each source when requested`(sourceCount: Int) {
        val source = mock<Source<String>>()
        val caches = List<Cache<String>>(sourceCount - 1) { mock() }

        Repository(source, *caches.toTypedArray()).request()

        assertThat(caches + source).allSatisfy { verify(it).request() }
    }

    @ParameterizedTest
    @MethodSource("subscribeToSourcesUntilFirstValueData")
    fun `repository should subscribe to sources in order until the first of them returns value`(expectedSubscribedSources: Int, sourceResults: List<SourceResult>) {
        val sourceResultSpy = sourceResults
                .last()
                .createMaybeSpy()
        val sourceMock = mock<Source<String>> {
            on { request() } doReturn sourceResultSpy
        }
        val cacheResultSpies = sourceResults
                .dropLast(1)
                .map { it.createMaybeSpy() }
        val cacheMocks = cacheResultSpies
                .map { resultSpy ->
                    mock<Cache<String>> {
                        on { request() } doReturn resultSpy
                    }
                }
        val allResultSpies = cacheResultSpies + sourceResultSpy

        Repository(sourceMock, *cacheMocks.toTypedArray()).request()

        assertThat(allResultSpies.take(expectedSubscribedSources)).allSatisfy { verify(it).subscribe(any<MaybeObserver<String>>()) }
        assertThat(allResultSpies.drop(expectedSubscribedSources)).allSatisfy { verifyZeroInteractions(it) }
    }

    companion object {
        @JvmStatic
        fun subscribeToSourcesUntilFirstValueData() = arrayOf(
                arguments(1, listOf(EMPTY)),
                arguments(1, listOf(VALUE)),
                arguments(1, listOf(ERROR)),
                arguments(1, listOf(VALUE, EMPTY)),
                arguments(1, listOf(VALUE, VALUE)),
                arguments(1, listOf(VALUE, VALUE, VALUE, EMPTY, ERROR, VALUE)),
                arguments(2, listOf(EMPTY, EMPTY)),
                arguments(2, listOf(EMPTY, VALUE)),
                arguments(2, listOf(EMPTY, VALUE, VALUE, VALUE)),
                arguments(5, listOf(EMPTY, EMPTY, EMPTY, EMPTY, VALUE, VALUE, ERROR))
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