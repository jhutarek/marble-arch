package cz.jhutarek.marble.example.current.data

import cz.jhutarek.marble.arch.repository.data.Cache
import cz.jhutarek.marble.example.current.domain.CurrentWeatherRepository
import cz.jhutarek.marble.example.current.model.CurrentWeather
import io.reactivex.Completable
import io.reactivex.Maybe
import org.threeten.bp.Duration
import javax.inject.Inject
import javax.inject.Singleton

// TODO add system time device (controller + usecase)
// TODO extract base classes
// TODO test
@Singleton
class CurrentWeatherMemoryCache @Inject constructor() : Cache<CurrentWeatherRepository.Query, CurrentWeather> {

    private val timeToLive = Duration.ofSeconds(10)

    private data class TimestampedCurrentWeather(val timestamp: Long, val currentWeather: CurrentWeather)

    private var value: TimestampedCurrentWeather? = null

    override fun request(query: CurrentWeatherRepository.Query): Maybe<CurrentWeather> = Maybe.fromCallable<CurrentWeather> {
        if (Duration.ofMillis(value?.timestamp ?: 0).plus(timeToLive).minusMillis(System.currentTimeMillis()) > Duration.ZERO) {
            value?.currentWeather
        } else {
            value = null
            null
        }
    }

    override fun store(query: CurrentWeatherRepository.Query, data: CurrentWeather): Completable = Completable.fromAction {
        value = TimestampedCurrentWeather(System.currentTimeMillis(), data)
    }

    override fun clear(query: CurrentWeatherRepository.Query?): Completable = Completable.fromAction {
        value = null
    }
}