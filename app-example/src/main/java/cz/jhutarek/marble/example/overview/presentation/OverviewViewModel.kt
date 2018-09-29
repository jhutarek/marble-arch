package cz.jhutarek.marble.example.overview.presentation

import cz.jhutarek.marble.arch.mvvm.presentation.ViewModel
import cz.jhutarek.marble.arch.repository.model.Data
import cz.jhutarek.marble.example.current.domain.CurrentWeatherUseCase
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import cz.jhutarek.marble.arch.mvvm.model.State as MarbleState

@Singleton
class OverviewViewModel @Inject constructor(
        private val observeCurrentWeather: CurrentWeatherUseCase.Observe,
        private val loadCurrentWeather: CurrentWeatherUseCase.Load
) : ViewModel<OverviewViewModel.State>(State()) {

    data class State(
            val input: String? = null,
            val refreshEnabled: Boolean = true
    ) : MarbleState

    init {
        observeCurrentWeather(Unit)
                .map {
                    State(
                            input = if (it is Data.Loaded) it.value.location else statesRelay.value.input,
                            refreshEnabled = it is Data.Empty || it is Data.Loaded || it is Data.Error
                    )
                }
                .subscribe(statesRelay)
    }

    fun refresh() {
        Timber.d("Refresh")

        loadCurrentWeather(Unit)
    }

    fun setInput(input: CharSequence) {
        Timber.d("Input: $input")

        statesRelay.accept { it.copy(input = input.toString()) }
    }
}