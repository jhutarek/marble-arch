package cz.jhutarek.marble.example.overview.presentation

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import cz.jhutarek.marble.example.current.domain.CurrentWeatherUseCase
import org.junit.jupiter.api.Test

internal class OverviewViewModelTest {

    private val anyLoadCurrentWeather = mock<CurrentWeatherUseCase.Load>()
    private val viewModel = OverviewViewModel(anyLoadCurrentWeather)

    @Test
    fun `should execute load current weather use case on refresh`() {
        viewModel.refresh()

        verify(anyLoadCurrentWeather).invoke(Unit)
    }
}