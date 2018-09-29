package cz.jhutarek.marble.example.current.domain

import com.nhaarman.mockitokotlin2.*
import cz.jhutarek.marble.arch.repository.model.Data
import cz.jhutarek.marble.example.current.domain.CurrentWeatherUseCase.Load
import cz.jhutarek.marble.example.current.domain.CurrentWeatherUseCase.Observe
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
        private val observe = Observe(anyRepository)

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
        private val anyByCity = Load.ByCity("any city")
        private val load = Load(anyRepository)

        @Test
        fun `should execute load on repository`() {
            load(anyByCity)

            verify(anyRepository).load(any())
        }
    }
}