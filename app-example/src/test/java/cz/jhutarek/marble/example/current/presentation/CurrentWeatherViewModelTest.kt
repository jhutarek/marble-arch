package cz.jhutarek.marble.example.current.presentation

import cz.jhutarek.marble.arch.repository.model.Data
import cz.jhutarek.marble.arch.resources.domain.StringsUseCase
import cz.jhutarek.marble.arch.test.infrastructure.InstancePerClassStringSpec
import cz.jhutarek.marble.example.current.domain.CurrentWeatherUseCase
import cz.jhutarek.marble.example.current.model.CurrentWeather
import cz.jhutarek.marble.example.current.model.CurrentWeather.DescriptionCode.*
import cz.jhutarek.marble.example.current.presentation.CurrentWeatherViewModel.State
import cz.jhutarek.marblearch.R
import io.kotlintest.data.forall
import io.kotlintest.tables.row
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

internal class CurrentWeatherViewModelTest : InstancePerClassStringSpec({
    val error = IllegalStateException("error")
    val currentWeather = CurrentWeather(
        ZonedDateTime.of(2018, 10, 10, 10, 10, 10, 10, ZoneId.systemDefault()),
        "location",
        15.6,
        123.4,
        "description",
        UNKNOWN,
        21.0,
        180.0,
        ZonedDateTime.of(2018, 9, 9, 9, 9, 9, 9, ZoneId.systemDefault()),
        ZonedDateTime.of(2018, 11, 11, 11, 11, 11, 11, ZoneId.systemDefault())
    )
    val errorString = "error string: %s"
    val temperatureString = "temperature string: %.2f"
    val pressureString = "pressure string: %.2f"
    val windSpeedString = "wind speed string: %.2f"
    val windDirectionString = "wind direction string: %.2f"
    val sunriseString = "sunrise string: %s"
    val sunsetString = "sunset string: %s"
    val additionalInfoSeparatorString = " | "

    val observeCurrentWeather = mockk<CurrentWeatherUseCase.Observe> {
        every { this@mockk(any()) } returns Observable.never<Data<Unit, CurrentWeather>>()
    }
    val getString = mockk<StringsUseCase.GetString> {
        every { this@mockk(R.string.current__error) } returns errorString
        every { this@mockk(R.string.current__temperature) } returns temperatureString
        every { this@mockk(R.string.current__pressure) } returns pressureString
        every { this@mockk(R.string.current__wind_speed) } returns windSpeedString
        every { this@mockk(R.string.current__wind_direction) } returns windDirectionString
        every { this@mockk(R.string.current__sunrise) } returns sunriseString
        every { this@mockk(R.string.current__sunset) } returns sunsetString
        every { this@mockk(R.string.current__additional_info_separator) } returns additionalInfoSeparatorString
    }

    "view model should execute observe use case in constructor" {
        CurrentWeatherViewModel(observeCurrentWeather, getString)

        verify { observeCurrentWeather(Unit) }
    }

    "view model should map repository emissions to states" {
        forall(
            row(
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
                    descriptionText = null,
                    additionalInfo = null
                ),
                Data.Empty(Unit)
            ),
            row(
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
                    descriptionText = null,
                    additionalInfo = null
                ),
                Data.Loading(Unit)
            ),
            row(
                State(
                    loadingVisible = false,
                    emptyVisible = false,
                    error = errorString.format(error.toString()),
                    errorVisible = true,
                    dataVisible = false,
                    timestamp = null,
                    location = null,
                    temperature = null,
                    pressure = null,
                    descriptionText = null,
                    additionalInfo = null
                ),
                Data.Error(Unit, error)
            ),
            row(
                State(
                    loadingVisible = false,
                    emptyVisible = false,
                    error = null,
                    errorVisible = false,
                    dataVisible = true,
                    timestamp = currentWeather.timestamp?.format(DateTimeFormatter.ofPattern("dd. LLLL H:mm")),
                    location = currentWeather.location,
                    temperature = temperatureString.format(currentWeather.temperatureCelsius),
                    pressure = pressureString.format(currentWeather.pressureMilliBar),
                    descriptionText = currentWeather.descriptionText?.capitalize(),
                    additionalInfo = listOf(
                        windSpeedString.format(currentWeather.windSpeedKmph),
                        windDirectionString.format(currentWeather.windDirectionDegrees),
                        currentWeather.sunriseTimestamp?.format(DateTimeFormatter.ofPattern("H:mm"))?.let { sunriseString.format(it) },
                        currentWeather.sunsetTimestamp?.format(DateTimeFormatter.ofPattern("H:mm"))?.let { sunsetString.format(it) }
                    ).joinToString(separator = additionalInfoSeparatorString)
                ),
                Data.Loaded(Unit, currentWeather)
            )
        ) { expectedState, data ->
            every { observeCurrentWeather(Unit) } returns Observable.just(data)

            CurrentWeatherViewModel(observeCurrentWeather, getString).states
                .test()
                .assertValue(expectedState)
        }
    }

    "view model should map description codes to icons" {
        forall(
            row(R.drawable.ic_clear, CLEAR),
            row(R.drawable.ic_fog, FOG),
            row(R.drawable.ic_few_clouds, FEW_CLOUDS),
            row(R.drawable.ic_scattered_clouds, SCATTERED_CLOUDS),
            row(R.drawable.ic_overcast_clouds, OVERCAST_CLOUDS),
            row(R.drawable.ic_drizzle, DRIZZLE),
            row(R.drawable.ic_light_rain, LIGHT_RAIN),
            row(R.drawable.ic_heavy_rain, HEAVY_RAIN),
            row(R.drawable.ic_snow, SNOW),
            row(R.drawable.ic_thunderstorm, THUNDERSTORM),
            row(R.drawable.ic_unknown, UNKNOWN)
        ) { expectedResId, descriptionCode ->
            every { observeCurrentWeather(Unit) } returns
                    Observable.just(Data.Loaded(Unit, currentWeather.copy(descriptionCode = descriptionCode)))

            CurrentWeatherViewModel(observeCurrentWeather, getString).states
                .test()
                .assertValue { it.descriptionIcon == expectedResId }
        }
    }
})