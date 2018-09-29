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

    private data class TimestampedCurrentWeather(val timestamp: Long, val currentWeather: CurrentWeather)

    private val timeToLive = Duration.ofSeconds(30)

    private val values = mutableMapOf<CurrentWeatherRepository.Query, TimestampedCurrentWeather>()

    override fun request(query: CurrentWeatherRepository.Query): Maybe<CurrentWeather> = Maybe.fromCallable<CurrentWeather> {
        values[query]?.let {
            if (Duration.ofMillis(it.timestamp).plus(timeToLive).minusMillis(System.currentTimeMillis()) > Duration.ZERO) {
                it.currentWeather
            } else {
                values.remove(query)
                null
            }
        }
    }

    override fun store(query: CurrentWeatherRepository.Query, data: CurrentWeather): Completable = Completable.fromAction {
        values[query] = TimestampedCurrentWeather(System.currentTimeMillis(), data)
    }

    override fun clear(query: CurrentWeatherRepository.Query?): Completable = Completable.fromAction {
        values.clear()
    }
}