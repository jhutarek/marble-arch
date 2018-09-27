package cz.jhutarek.marble.example.current.data

import cz.jhutarek.marble.arch.repository.model.Data
import cz.jhutarek.marble.example.current.domain.CurrentWeatherRepository
import cz.jhutarek.marble.example.current.domain.CurrentWeatherRepository.Query
import cz.jhutarek.marble.example.current.model.CurrentWeather
import io.reactivex.Completable
import io.reactivex.Observable
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidCurrentWeatherRepository @Inject constructor() : CurrentWeatherRepository {

    override fun observe(): Observable<Data<CurrentWeatherRepository.Query, CurrentWeather>> {
        // TODO
        return Observable.just(
                Data.Loaded(
                        Query(),
                        CurrentWeather(
                                timestamp = ZonedDateTime.now(),
                                location = "Somewhere",
                                temperatureCelsius = 14.5,
                                pressureMilliBar = 987.4,
                                description = "Mildly moist",
                                windSpeedKmph = 15.6,
                                windDirectionDegrees = 169.0,
                                sunriseTimestamp = ZonedDateTime.now().withHour(6),
                                sunsetTimestamp = ZonedDateTime.now().withHour(20)
                        )
                )
        )
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