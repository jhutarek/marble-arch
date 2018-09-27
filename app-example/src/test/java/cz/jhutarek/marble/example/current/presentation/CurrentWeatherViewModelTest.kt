package cz.jhutarek.marble.example.current.presentation

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import cz.jhutarek.marble.example.current.domain.CurrentWeatherRepository.Query
import cz.jhutarek.marble.example.current.domain.CurrentWeatherUseCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

internal class CurrentWeatherViewModelTest {

    // TODO extract to separate file
    abstract class BaseArgumentsProvider(private val provider: () -> Stream<out Arguments>) : ArgumentsProvider {
        final override fun provideArguments(context: ExtensionContext): Stream<out Arguments> = provider()
    }

    companion object {
        val anyError = IllegalStateException("any error")
        val anyQuery = Query()
    }

    private val anyObserve = mock<CurrentWeatherUseCase.Observe>()

    @Test
    fun `should execute observe use case on construction`() {
        CurrentWeatherViewModel(anyObserve)

        verify(anyObserve).invoke(Unit)
    }

    // TODO
    /*@ParameterizedTest
    @ArgumentsSource(ShouldMapRepositoryEmissionsProvider::class)
    fun `should map repository emissions to states`(expectedState: CurrentWeatherViewModel.State, data: Data<CurrentWeatherRepository.Query, CurrentWeather>) {
        whenever(anyObserve.execute(Unit)).thenReturn(Observable.just(data))

        CurrentWeatherViewModel(anyObserve).states
                .test()
                .assertValue(expectedState)
    }*/

    /*
    internal class ShouldMapRepositoryEmissionsProvider : BaseArgumentsProvider({
        Stream.of(
                arguments(State(
                        emptyVisible = true
                ), Data.Empty(anyQuery)),
                arguments(State(emptyVisible = false, loadingVisible = true), Data.Loading(anyQuery)),
                arguments(State(emptyVisible = false, error), Data.Error(anyQuery, anyError)),
                arguments(State(), Data.Loaded(anyQuery, CurrentWeather()))
        )
    })*/
}