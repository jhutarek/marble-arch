package cz.jhutarek.marble.arch.repository.data

import com.nhaarman.mockitokotlin2.*
import cz.jhutarek.marble.arch.repository.data.BaseRepositoryTest.MockRepositoryBuilder.SourceResult
import cz.jhutarek.marble.arch.repository.data.BaseRepositoryTest.MockRepositoryBuilder.SourceResult.*
import cz.jhutarek.marble.arch.repository.data.BaseRepositoryTest.MockRepositoryBuilder.SourceResult.Error.Companion.EXPECTED_ERROR
import cz.jhutarek.marble.arch.repository.data.BaseRepositoryTest.MockRepositoryBuilder.SourceResult.Value.Companion.EXPECTED_VALUE
import cz.jhutarek.marble.arch.repository.model.Data
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.MaybeObserver
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream

internal class BaseRepositoryTest {

    // TODO extract to separate file
    abstract class BaseArgumentsProvider(private val provider: () -> Stream<out Arguments>) : ArgumentsProvider {
        final override fun provideArguments(context: ExtensionContext): Stream<out Arguments> = provider()
    }

    internal class MockRepositoryBuilder(allResults: List<SourceResult> = listOf(Whatever())) {
        val anyKey = "any key"

        internal sealed class SourceResult {
            abstract val maybeSpy: Maybe<String>

            class Whatever : SourceResult() {
                override val maybeSpy: Maybe<String> = spy(Maybe.empty())
                override fun toString() = "any"
            }

            class Empty : SourceResult() {
                override val maybeSpy: Maybe<String> = spy(Maybe.empty())
                override fun toString() = "empty"
            }

            data class Value(private val value: String = ANY_VALUE) : SourceResult() {
                companion object {
                    const val ANY_VALUE = "any value"
                    const val EXPECTED_VALUE = "expected value"
                }

                override val maybeSpy: Maybe<String> = spy(Maybe.just(value))
                override fun toString() = value
            }

            data class Error(private val error: Throwable = ANY_ERROR) : SourceResult() {
                companion object {
                    val ANY_ERROR = IllegalStateException("any error")
                    val EXPECTED_ERROR = IllegalStateException("expected error")
                }

                override val maybeSpy: Maybe<String> = spy(Maybe.error(error))
                override fun toString() = "$error"
            }
        }

        private val sourceResult = allResults.last()
        private val cacheResults = allResults.dropLast(1)

        private val sourceMock = mock<Source<String, String>> { on { request(anyKey) } doReturn sourceResult.maybeSpy }
        private val sourceResultSpy = sourceResult.maybeSpy

        val cacheMocks = cacheResults.map { result ->
            mock<Cache<String, String>> {
                on { request(anyKey) } doReturn result.maybeSpy
                on { store(eq(anyKey), any()) } doReturn Completable.complete()
            }
        }
        private val cacheResultSpies = cacheResults.map { it.maybeSpy }

        val allMocks = cacheMocks + sourceMock
        val allResultSpies = cacheResultSpies + sourceResultSpy

        val repository = object : BaseRepository<String, String>(sourceMock, *cacheMocks.toTypedArray()) {}
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 2, 3, 10])
    fun `repository should request each source when requested`(sourceCount: Int) {
        MockRepositoryBuilder(List(sourceCount) { Whatever() }).run {
            repository.request(anyKey)

            assertThat(allMocks).allSatisfy { verify(it).request(anyKey) }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(SubscribeToSourcesUntilFirstValueProvider::class)
    fun `repository should subscribe to sources until the first of them returns value`(expectedSubscribedSources: Int, allResults: List<SourceResult>) {
        MockRepositoryBuilder(allResults).run {
            repository.request(anyKey)

            assertThat(allResultSpies.take(expectedSubscribedSources)).allSatisfy { verify(it).subscribe(any<MaybeObserver<String>>()) }
            assertThat(allResultSpies.drop(expectedSubscribedSources)).allSatisfy { verify(it, never()).subscribe(any<MaybeObserver<String>>()) }
        }
    }

    internal class SubscribeToSourcesUntilFirstValueProvider : BaseArgumentsProvider({
        Stream.of(
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
    })

    @Test
    fun `repository should emit loading first when requested`() {
        MockRepositoryBuilder().run {
            val testObserver = repository.observe().test()

            repository.request(anyKey)

            testObserver.assertValueAt(0, Data.Loading(anyKey))
        }
    }

    @ParameterizedTest
    @ArgumentsSource(ValueFromFirstNonEmptySourceProvider::class)
    fun `repository should emit value from first source that is not empty or error`(allResults: List<SourceResult>) {
        MockRepositoryBuilder(allResults).run {
            val testObserver = repository.observe().test()

            repository.request(anyKey)

            testObserver.assertValueAt(1, Data.Loaded(anyKey, EXPECTED_VALUE))
        }
    }

    internal class ValueFromFirstNonEmptySourceProvider : BaseArgumentsProvider({
        Stream.of(
                listOf(Value(EXPECTED_VALUE)),
                listOf(Value(EXPECTED_VALUE), Empty()),
                listOf(Value(EXPECTED_VALUE), Value()),
                listOf(Value(EXPECTED_VALUE), Value(), Value(), Empty(), Error(), Value()),
                listOf(Empty(), Value(EXPECTED_VALUE)),
                listOf(Empty(), Value(EXPECTED_VALUE), Value(), Value()),
                listOf(Empty(), Empty(), Empty(), Empty(), Value(EXPECTED_VALUE), Value(), Error())
        ).map { arguments(it) }
    })

    @ParameterizedTest
    @ArgumentsSource(ErrorFromFirstNonEmptySourceProvider::class)
    fun `repository should emit error from first source that is not empty or has value`(allResults: List<SourceResult>) {
        MockRepositoryBuilder(allResults).run {
            val testObserver = repository.observe().test()

            repository.request(anyKey)

            testObserver.assertValueAt(1, Data.Error(anyKey, EXPECTED_ERROR))
        }
    }

    internal class ErrorFromFirstNonEmptySourceProvider : BaseArgumentsProvider({
        Stream.of(
                listOf(Error(EXPECTED_ERROR)),
                listOf(Error(EXPECTED_ERROR), Empty()),
                listOf(Error(EXPECTED_ERROR), Error()),
                listOf(Error(EXPECTED_ERROR), Error(), Value(), Empty(), Error(), Value()),
                listOf(Empty(), Error(EXPECTED_ERROR)),
                listOf(Empty(), Error(EXPECTED_ERROR), Error(), Value()),
                listOf(Empty(), Empty(), Empty(), Empty(), Error(EXPECTED_ERROR), Value(), Error())
        ).map { arguments(it) }
    })

    @ParameterizedTest
    @ValueSource(ints = [1, 2, 3, 10])
    fun `repository should emit empty if all sources are empty`(sourceCount: Int) {
        MockRepositoryBuilder(List(sourceCount) { Empty() }).run {
            val testObserver = repository.observe().test()

            repository.request(anyKey)

            testObserver.assertValueAt(1, Data.Empty(anyKey))
        }
    }

    @ParameterizedTest
    @ArgumentsSource(StoreValueInHigherCachesProvider::class)
    fun `repository should store value loaded from lower source in all higher caches`(expectedStoreCount: Int, allResults: List<SourceResult>) {
        MockRepositoryBuilder(allResults).run {
            repository.request(anyKey)

            assertThat(cacheMocks.take(expectedStoreCount)).allSatisfy { verify(it).store(anyKey, EXPECTED_VALUE) }
            assertThat(cacheMocks.drop(expectedStoreCount)).allSatisfy { verify(it, never()).store(eq(anyKey), any()) }
        }
    }

    internal class StoreValueInHigherCachesProvider : BaseArgumentsProvider({
        Stream.of(
                arguments(0, listOf(Empty())),
                arguments(0, listOf(Value())),
                arguments(0, listOf(Error())),
                arguments(0, listOf(Value(), Empty())),
                arguments(0, listOf(Value(), Value())),
                arguments(0, listOf(Empty(), Empty())),
                arguments(1, listOf(Empty(), Value(EXPECTED_VALUE))),
                arguments(2, listOf(Empty(), Empty(), Value(EXPECTED_VALUE), Empty(), Error(), Value())),
                arguments(4, listOf(Empty(), Empty(), Empty(), Empty(), Value(EXPECTED_VALUE), Value(), Error()))
        )
    })

    @Test
    fun `repository should emit error if cache emits error when storing value`() {
        MockRepositoryBuilder(listOf(Empty(), Value())).run {
            whenever(cacheMocks[0].store(eq(anyKey), any())).thenReturn(Completable.error(EXPECTED_ERROR))
            val testObserver = repository.observe().test()

            repository.request(anyKey)

            testObserver.assertValueAt(1, Data.Error(anyKey, EXPECTED_ERROR))
        }
    }
}