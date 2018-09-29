package cz.jhutarek.marble.example.current.domain

import cz.jhutarek.marble.arch.repository.model.Data
import cz.jhutarek.marble.arch.usecase.domain.UseCase
import cz.jhutarek.marble.example.current.domain.CurrentWeatherRepository.Query
import cz.jhutarek.marble.example.current.model.CurrentWeather
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

sealed class CurrentWeatherUseCase<in I, out O> : UseCase<I, O> {

    @Singleton
    class Observe @Inject constructor(private val repository: CurrentWeatherRepository) : CurrentWeatherUseCase<Unit, Observable<Data<Unit, CurrentWeather>>>() {

        override operator fun invoke(input: Unit): Observable<Data<Unit, CurrentWeather>> = repository.observe().map { it.mapQuery { Unit } } // TODO add Observable.mapQuery extension
    }

    @Singleton
    class Load @Inject constructor(private val repository: CurrentWeatherRepository) : CurrentWeatherUseCase<Load.ByCity, Unit>() {

        data class ByCity(val city: String)

        override operator fun invoke(input: ByCity) = repository.load(Query(input.city))
    }
}