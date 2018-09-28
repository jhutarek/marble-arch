package cz.jhutarek.marble.example.current.data

import cz.jhutarek.marble.arch.repository.data.Source
import cz.jhutarek.marble.example.current.domain.CurrentWeatherRepository
import cz.jhutarek.marble.example.current.model.CurrentWeather
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers.computation
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

// TODO implement, test
@Singleton
class CurrentWeatherSource @Inject constructor() : Source<CurrentWeatherRepository.Query, CurrentWeather> {

    override fun request(query: CurrentWeatherRepository.Query): Maybe<CurrentWeather> = Maybe.fromCallable {
        Thread.sleep(1000)

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
    }
            .subscribeOn(computation())
}