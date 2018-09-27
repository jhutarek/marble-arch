package cz.jhutarek.marble.example.current.presentation

import cz.jhutarek.marble.arch.mvvm.presentation.ViewModel
import cz.jhutarek.marble.arch.repository.model.Data
import cz.jhutarek.marble.arch.resources.domain.StringsUseCase
import cz.jhutarek.marble.example.current.domain.CurrentWeatherUseCase
import cz.jhutarek.marblearch.R
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton
import cz.jhutarek.marble.arch.mvvm.model.State as MarbleState

@Singleton
class CurrentWeatherViewModel @Inject constructor(
        observe: CurrentWeatherUseCase.Observe,
        getString: StringsUseCase.GetString
) : ViewModel<CurrentWeatherViewModel.State>(State()) {

    data class State(
            val loadingVisible: Boolean = false,
            val emptyVisible: Boolean = false,
            val error: String? = null,
            val errorVisible: Boolean = false,
            val dataVisible: Boolean = false,
            val timestamp: String? = null,
            val location: String? = null,
            val temperature: String? = null,
            val pressure: String? = null,
            val descriptionText: String? = null,
            val windSpeed: String? = null,
            val windDirection: String? = null,
            val sunriseTimestamp: String? = null,
            val sunsetTimestamp: String? = null
    ) : MarbleState

    init {
        observe(Unit)
                .map {
                    when (it) {
                        is Data.Empty -> {
                            State(
                                    emptyVisible = true
                            )
                        }
                        is Data.Loading -> {
                            State(
                                    loadingVisible = true
                            )
                        }
                        is Data.Loaded -> {
                            State(
                                    dataVisible = true,
                                    timestamp = it.value.timestamp?.format(DateTimeFormatter.ofPattern("dd. LLLL H:mm")),
                                    location = it.value.location,
                                    temperature = it.value.temperatureCelsius?.let { getString(R.string.current__temperature).format(it) },
                                    pressure = it.value.pressureMilliBar?.let { getString(R.string.current__pressure).format(it) },
                                    descriptionText = it.value.description,
                                    windSpeed = it.value.windSpeedKmph?.let { getString(R.string.current__wind_speed).format(it) },
                                    windDirection = it.value.windDirectionDegrees?.let { getString(R.string.current__wind_direction).format(it) },
                                    sunriseTimestamp = it.value.sunriseTimestamp?.format(DateTimeFormatter.ofPattern("H:mm")),
                                    sunsetTimestamp = it.value.sunsetTimestamp?.format(DateTimeFormatter.ofPattern("H:mm"))
                            )
                        }
                        is Data.Error -> {
                            State(
                                    errorVisible = true,
                                    error = it.error.toString().let { getString(R.string.current__error).format(it) }
                            )
                        }
                    }
                }
                .subscribe(statesRelay)
    }
}