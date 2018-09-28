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
            val description: String? = null,
            val additionalInfo: String? = null
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
                                    description = it.value.description,
                                    additionalInfo = listOfNotNull(
                                            it.value.windSpeedKmph?.let { getString(R.string.current__wind_speed).format(it) },
                                            it.value.windDirectionDegrees?.let { getString(R.string.current__wind_direction).format(it) },
                                            it.value.sunriseTimestamp?.format(DateTimeFormatter.ofPattern("H:mm"))?.let { getString(R.string.current__sunrise).format(it) },
                                            it.value.sunsetTimestamp?.format(DateTimeFormatter.ofPattern("H:mm"))?.let { getString(R.string.current__sunset).format(it) }
                                    ).joinToString(separator = getString(R.string.current__additional_info_separator))
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