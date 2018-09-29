package cz.jhutarek.marble.example.overview.presentation

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import cz.jhutarek.marble.arch.repository.model.Data
import cz.jhutarek.marble.example.current.domain.CurrentWeatherUseCase
import cz.jhutarek.marble.example.current.model.CurrentWeather
import cz.jhutarek.marble.example.current.presentation.CurrentWeatherViewModelTest
import io.reactivex.Observable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class OverviewViewModelTest {

    // TODO extract to separate file
    abstract class BaseArgumentsProvider(private val provider: () -> Stream<out Arguments>) : ArgumentsProvider {
        final override fun provideArguments(context: ExtensionContext): Stream<out Arguments> = provider()
    }

    companion object {
        private val anyCurrentWeather = mock<CurrentWeather>()
        private val anyError = IllegalStateException()
        private val anyObserve = mock<CurrentWeatherUseCase.Observe> {
            on { invoke(Unit) } doReturn Observable.never()
        }
        private val anyLoadCurrentWeather = mock<CurrentWeatherUseCase.Load>()
    }

    @Test
    fun `should execute observe use case on construction`() {
        OverviewViewModel(anyObserve, anyLoadCurrentWeather)

        verify(anyObserve).invoke(Unit)
    }

    @ParameterizedTest
    @ArgumentsSource(ShouldMapDataToRefreshEnabledStateProvider::class)
    fun `should map observed data to refresh enabled state`(expectedEnabled: Boolean, data: Data<Unit, CurrentWeather>) {
        whenever(anyObserve.invoke(Unit)).thenReturn(Observable.just(data))

        OverviewViewModel(anyObserve, anyLoadCurrentWeather).states
                .test()
                .assertValueAt(0) { it.refreshEnabled == expectedEnabled }
    }

    internal class ShouldMapDataToRefreshEnabledStateProvider : CurrentWeatherViewModelTest.BaseArgumentsProvider({
        Stream.of(
                arguments(true, Data.Empty(Unit)),
                arguments(false, Data.Loading(Unit)),
                arguments(true, Data.Loaded(Unit, anyCurrentWeather)),
                arguments(true, Data.Error(Unit, anyError))
        )
    })

    @Test
    fun `should execute load current weather use case on refresh`() {
        OverviewViewModel(anyObserve, anyLoadCurrentWeather).refresh()

        verify(anyLoadCurrentWeather).invoke(Unit)
    }
}