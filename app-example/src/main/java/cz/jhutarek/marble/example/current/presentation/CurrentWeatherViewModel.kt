package cz.jhutarek.marble.example.current.presentation

import cz.jhutarek.marble.arch.mvvm.presentation.ViewModel
import cz.jhutarek.marble.example.current.domain.CurrentWeatherUseCase
import javax.inject.Inject
import javax.inject.Singleton
import cz.jhutarek.marble.arch.mvvm.model.State as MarbleState

@Singleton
class CurrentWeatherViewModel @Inject constructor(
        observe: CurrentWeatherUseCase.Observe
) : ViewModel<CurrentWeatherViewModel.State>(State()) {

    data class State(
            val loadingVisible: Boolean = false,
            val emptyVisible: Boolean = true,
            val error: String? = null,
            val errorVisible: String? = null,
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
    }
}