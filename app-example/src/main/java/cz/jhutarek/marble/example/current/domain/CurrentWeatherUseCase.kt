package cz.jhutarek.marble.example.current.domain

import cz.jhutarek.marble.arch.repository.model.Data
import cz.jhutarek.marble.arch.usecase.domain.UseCase
import cz.jhutarek.marble.example.current.model.CurrentWeather
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

sealed class CurrentWeatherUseCase<in I, out O> : UseCase<I, O> {

    @Singleton
    class Observe @Inject constructor(
            private val repository: CurrentWeatherRepository
    ) : CurrentWeatherUseCase<Unit, Observable<Data<CurrentWeatherRepository.Query, CurrentWeather>>>() {
        override fun execute(input: Unit): Observable<Data<CurrentWeatherRepository.Query, CurrentWeather>> = repository.observe()
    }

    @Singleton
    class Load @Inject constructor(
            private val repository: CurrentWeatherRepository
    ) : CurrentWeatherUseCase<Unit, Unit>() {
        override fun execute(input: Unit) = repository.load(CurrentWeatherRepository.Query())
    }
}