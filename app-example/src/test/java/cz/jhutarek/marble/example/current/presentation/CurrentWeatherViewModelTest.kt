package cz.jhutarek.marble.example.current.presentation

import com.nhaarman.mockitokotlin2.*
import cz.jhutarek.marble.arch.repository.model.Data
import cz.jhutarek.marble.arch.resources.domain.StringsUseCase
import cz.jhutarek.marble.example.current.domain.CurrentWeatherRepository
import cz.jhutarek.marble.example.current.domain.CurrentWeatherRepository.Query
import cz.jhutarek.marble.example.current.domain.CurrentWeatherUseCase
import cz.jhutarek.marble.example.current.model.CurrentWeather
import cz.jhutarek.marble.example.current.presentation.CurrentWeatherViewModel.State
import cz.jhutarek.marblearch.R
import io.reactivex.Observable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.stream.Stream

internal class CurrentWeatherViewModelTest {

    // TODO extract to separate file
    abstract class BaseArgumentsProvider(private val provider: () -> Stream<out Arguments>) : ArgumentsProvider {
        final override fun provideArguments(context: ExtensionContext): Stream<out Arguments> = provider()
    }

    companion object {
        val anyError = IllegalStateException("any error")
        val anyQuery = Query()
        val anyCurrentWeather = CurrentWeather(
                ZonedDateTime.of(2018, 10, 10, 10, 10, 10, 10, ZoneId.systemDefault()),
                "any location",
                15.6,
                123.4,
                "any description",
                21.0,
                180.0,
                ZonedDateTime.of(2018, 9, 9, 9, 9, 9, 9, ZoneId.systemDefault()),
                ZonedDateTime.of(2018, 11, 11, 11, 11, 11, 11, ZoneId.systemDefault())
        )
        const val anyErrorString = "any error string: %s"
        const val anyTemperatureString = "any temperature string: %.2f"
        const val anyPressureString = "any pressure string: %.2f"
        const val anyWindSpeedString = "any wind speed string: %.2f"
        const val anyWindDirectionString = "any wind direction string: %.2f"
        const val anySunriseString = "any sunrise string: %s"
        const val anySunsetString = "any sunset string: %s"
    }

    private val anyObserve = mock<CurrentWeatherUseCase.Observe> {
        on { invoke(any()) } doReturn Observable.never<Data<CurrentWeatherRepository.Query, CurrentWeather>>()
    }
    private val anyGetString = mock<StringsUseCase.GetString> {
        on { invoke(R.string.current__error) } doReturn anyErrorString
        on { invoke(R.string.current__temperature) } doReturn anyTemperatureString
        on { invoke(R.string.current__pressure) } doReturn anyPressureString
        on { invoke(R.string.current__wind_speed) } doReturn anyWindSpeedString
        on { invoke(R.string.current__wind_direction) } doReturn anyWindDirectionString
        on { invoke(R.string.current__sunrise) } doReturn anySunriseString
        on { invoke(R.string.current__sunset) } doReturn anySunsetString
    }

    @Test
    fun `should execute observe use case on construction`() {
        CurrentWeatherViewModel(anyObserve, anyGetString)

        verify(anyObserve).invoke(Unit)
    }

    @ParameterizedTest
    @ArgumentsSource(ShouldMapRepositoryEmissionsProvider::class)
    fun `should map repository emissions to states`(expectedState: CurrentWeatherViewModel.State, data: Data<CurrentWeatherRepository.Query, CurrentWeather>) {
        whenever(anyObserve(Unit)).thenReturn(Observable.just(data))

        CurrentWeatherViewModel(anyObserve, anyGetString).states
                .test()
                .assertValue(expectedState)
    }

    internal class ShouldMapRepositoryEmissionsProvider : BaseArgumentsProvider({
        Stream.of(
                arguments(
                        State(
                                loadingVisible = false,
                                emptyVisible = true,
                                error = null,
                                errorVisible = false,
                                dataVisible = false,
                                timestamp = null,
                                location = null,
                                temperature = null,
                                pressure = null,
                                description = null,
                                windSpeed = null,
                                windDirection = null,
                                sunriseTimestamp = null,
                                sunsetTimestamp = null
                        ),
                        Data.Empty(anyQuery)
                ),
                arguments(
                        State(
                                loadingVisible = true,
                                emptyVisible = false,
                                error = null,
                                errorVisible = false,
                                dataVisible = false,
                                timestamp = null,
                                location = null,
                                temperature = null,
                                pressure = null,
                                description = null,
                                windSpeed = null,
                                windDirection = null,
                                sunriseTimestamp = null,
                                sunsetTimestamp = null
                        ),
                        Data.Loading(anyQuery)
                ),
                arguments(
                        State(
                                loadingVisible = false,
                                emptyVisible = false,
                                error = anyErrorString.format(anyError.toString()),
                                errorVisible = true,
                                dataVisible = false,
                                timestamp = null,
                                location = null,
                                temperature = null,
                                pressure = null,
                                description = null,
                                windSpeed = null,
                                windDirection = null,
                                sunriseTimestamp = null,
                                sunsetTimestamp = null
                        ),
                        Data.Error(anyQuery, anyError)
                ),
                arguments(
                        State(
                                loadingVisible = false,
                                emptyVisible = false,
                                error = null,
                                errorVisible = false,
                                dataVisible = true,
                                timestamp = anyCurrentWeather.timestamp?.format(DateTimeFormatter.ofPattern("dd. LLLL H:mm")),
                                location = anyCurrentWeather.location,
                                temperature = anyTemperatureString.format(anyCurrentWeather.temperatureCelsius),
                                pressure = anyPressureString.format(anyCurrentWeather.pressureMilliBar),
                                description = anyCurrentWeather.description,
                                windSpeed = anyWindSpeedString.format(anyCurrentWeather.windSpeedKmph),
                                windDirection = anyWindDirectionString.format(anyCurrentWeather.windDirectionDegrees),
                                sunriseTimestamp = anyCurrentWeather.sunriseTimestamp?.format(DateTimeFormatter.ofPattern("H:mm"))?.let { anySunriseString.format(it) },
                                sunsetTimestamp = anyCurrentWeather.sunsetTimestamp?.format(DateTimeFormatter.ofPattern("H:mm"))?.let { anySunsetString.format(it) }
                        ),
                        Data.Loaded(anyQuery, anyCurrentWeather)
                )
        )
    })
}