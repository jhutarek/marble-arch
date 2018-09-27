package cz.jhutarek.marble.example.current.domain

import com.nhaarman.mockitokotlin2.*
import cz.jhutarek.marble.arch.repository.model.Data
import cz.jhutarek.marble.example.current.model.CurrentWeather
import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CurrentWeatherUseCaseTest {

    private val anyRepository = mock<CurrentWeatherRepository>()

    @Nested
    inner class ObserveUseCase {
        private val useCase = CurrentWeatherUseCase.Observe(anyRepository)
        private val anyObservable = Observable.never<Data<CurrentWeatherRepository.Query, CurrentWeather>>()

        @Test
        fun `should execute observe on repository`() {
            useCase.execute(Unit)

            verify(anyRepository).observe()
        }

        @Test
        fun `should return observable from repository`() {
            whenever(anyRepository.observe()).doReturn(anyObservable)

            assertThat(useCase.execute(Unit)).isEqualTo(anyObservable)
        }
    }

    @Nested
    inner class LoadUseCase {
        private val useCase = CurrentWeatherUseCase.Load(anyRepository)

        @Test
        fun `should execute load on repository`() {
            useCase.execute(Unit)

            verify(anyRepository).load(any())
        }
    }
}