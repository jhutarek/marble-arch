package cz.jhutarek.marble.example.current.data

import cz.jhutarek.marble.arch.repository.model.Data
import cz.jhutarek.marble.example.current.domain.CurrentWeatherRepository
import cz.jhutarek.marble.example.current.model.CurrentWeather
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidCurrentWeatherRepository @Inject constructor() : CurrentWeatherRepository {

    override fun observe(): Observable<Data<CurrentWeatherRepository.Query, CurrentWeather>> {
        return Observable.never() // TODO
    }

    override fun load(query: CurrentWeatherRepository.Query) {
        // TODO
    }

    override fun update(query: CurrentWeatherRepository.Query) {
        // TODO
    }

    override fun clearCaches(query: CurrentWeatherRepository.Query?): Completable {
        return Completable.complete() // TODO
    }
}