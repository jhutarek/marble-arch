package cz.jhutarek.marble.example.overview.presentation

import cz.jhutarek.marble.arch.navigation.domain.NavigationUseCase
import cz.jhutarek.marble.arch.repository.model.Data
import cz.jhutarek.marble.test.infrastructure.InstancePerClassStringSpec
import cz.jhutarek.marble.example.current.domain.CurrentWeatherUseCase
import cz.jhutarek.marble.example.current.domain.CurrentWeatherUseCase.Load
import cz.jhutarek.marble.example.current.model.CurrentWeather
import cz.jhutarek.marblearch.R
import io.kotlintest.data.forall
import io.kotlintest.tables.row
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable

internal class OverviewViewModelTest : InstancePerClassStringSpec({
    val location = "location"
    val currentWeather = mockk<CurrentWeather> {
        every { this@mockk.location } returns location
    }
    val error = IllegalStateException()
    val input = "any input"
    val observe = mockk<CurrentWeatherUseCase.Observe> {
        every { this@mockk(Unit) } returns Observable.never()
    }
    val loadCurrentWeather = mockk<CurrentWeatherUseCase.Load>(relaxUnitFun = true)
    val navigate = mockk<NavigationUseCase.Navigate>(relaxUnitFun = true)

    "view model should execute observe use case in constructor" {
        OverviewViewModel(observe, loadCurrentWeather, navigate)

        verify { observe(Unit) }
    }

    "view model should map observed data to refresh enabled state" {
        forall(
            row(true, Data.Empty(Unit)),
            row(false, Data.Loading(Unit)),
            row(true, Data.Loaded(Unit, currentWeather)),
            row(true, Data.Error(Unit, error))
        ) { expectedEnabled, data ->
            every { observe(Unit) } returns Observable.just(data)

            OverviewViewModel(observe, loadCurrentWeather, navigate).states
                .test()
                .assertValueAt(0) { it.refreshEnabled == expectedEnabled }
        }
    }

    "view model should map observed data to input state when loaded" {
        every { observe(Unit) } returns Observable.just(Data.Loaded(Unit, currentWeather))

        OverviewViewModel(observe, loadCurrentWeather, navigate).states
            .test()
            .assertValueAt(0) { it.input == location }
    }

    "view model should execute load current weather use case on refresh with current input" {
        OverviewViewModel(observe, loadCurrentWeather, navigate).apply {
            setInput(input)

            refresh()
        }

        verify { loadCurrentWeather(Load.ByCity(input)) }
    }

    "view model should update input when set" {
        val viewModel = OverviewViewModel(observe, loadCurrentWeather, navigate)
        val testObserver = viewModel.states.test()

        viewModel.setInput(input)

        testObserver.assertValueAt(1) { it.input == input }
    }

    "view model should navigate to settings" {
        val viewModel = OverviewViewModel(observe, loadCurrentWeather, navigate)

        viewModel.showSettings()

        verify { navigate(R.id.navigation__settings) }
    }
})