package cz.jhutarek.marble.example.overview.presentation

import cz.jhutarek.marble.arch.log.infrastructure.logI
import cz.jhutarek.marble.arch.mvvm.presentation.ViewModel
import cz.jhutarek.marble.arch.navigation.domain.NavigationUseCase
import cz.jhutarek.marble.arch.repository.model.LegacyData
import cz.jhutarek.marble.example.current.domain.CurrentWeatherUseCase
import cz.jhutarek.marble.example.current.domain.CurrentWeatherUseCase.Load.ByCity
import cz.jhutarek.marblearch.R
import javax.inject.Inject
import javax.inject.Singleton
import cz.jhutarek.marble.arch.mvvm.model.State as MarbleState

@Singleton
class OverviewViewModel @Inject constructor(
    observeCurrentWeather: CurrentWeatherUseCase.Observe,
    private val loadCurrentWeather: CurrentWeatherUseCase.Load,
    private val navigate: NavigationUseCase.Navigate
) : ViewModel<OverviewViewModel.State>(State()) {

    data class State(
        val input: String? = null,
        val refreshEnabled: Boolean = true
    ) : MarbleState

    init {
        observeCurrentWeather(Unit)
            .map {
                State(
                    input = if (it is LegacyData.Loaded) it.value.location else statesRelay.value!!.input,
                    refreshEnabled = it is LegacyData.Empty || it is LegacyData.Loaded || it is LegacyData.Error
                )
            }
            .subscribe(statesRelay)
    }

    fun refresh() {
        logI("Refresh")

        loadCurrentWeather(ByCity(statesRelay.value!!.input.orEmpty()))
    }

    fun showSettings() {
        logI("Show settings")

        navigate(R.id.navigation__settings)
    }

    fun setInput(input: CharSequence) {
        logI("Input: $input")

        statesRelay.accept { it.copy(input = input.toString()) }
    }
}