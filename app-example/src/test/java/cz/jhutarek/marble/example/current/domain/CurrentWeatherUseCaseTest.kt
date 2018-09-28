package cz.jhutarek.marble.example.current.domain

import com.nhaarman.mockitokotlin2.*
import cz.jhutarek.marble.arch.repository.model.Data
import cz.jhutarek.marble.example.current.model.CurrentWeather
import io.reactivex.Observable
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CurrentWeatherUseCaseTest {

    private val anyRepository = mock<CurrentWeatherRepository>()

    @Nested
    inner class ObserveUseCase {
        private val anyRepositoryData: Data<CurrentWeatherRepository.Query, CurrentWeather> = Data.Loading(CurrentWeatherRepository.Query())
        private val expectedMappedData = Data.Loading(Unit)
        private val observe = CurrentWeatherUseCase.Observe(anyRepository)

        @Test
        fun `should execute observe on repository`() {
            whenever(anyRepository.observe()).thenReturn(Observable.never())

            observe(Unit)

            verify(anyRepository).observe()
        }

        @Test
        fun `should return mapped observable from repository`() {
            whenever(anyRepository.observe()).doReturn(Observable.just(anyRepositoryData))

            observe(Unit)
                    .test()
                    .assertValue(expectedMappedData)
        }
    }

    @Nested
    inner class LoadUseCase {
        private val load = CurrentWeatherUseCase.Load(anyRepository)

        @Test
        fun `should execute load on repository`() {
            load(Unit)

            verify(anyRepository).load(any())
        }
    }
}