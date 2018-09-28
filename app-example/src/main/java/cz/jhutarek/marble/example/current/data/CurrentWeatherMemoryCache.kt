package cz.jhutarek.marble.example.current.data

import cz.jhutarek.marble.arch.repository.data.Cache
import cz.jhutarek.marble.example.current.domain.CurrentWeatherRepository
import cz.jhutarek.marble.example.current.model.CurrentWeather
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers.computation
import javax.inject.Inject
import javax.inject.Singleton

// TODO extract base class, test
@Singleton
class CurrentWeatherMemoryCache @Inject constructor() : Cache<CurrentWeatherRepository.Query, CurrentWeather> {

    private var value: CurrentWeather? = null

    override fun request(query: CurrentWeatherRepository.Query): Maybe<CurrentWeather> = Maybe.fromCallable<CurrentWeather> {
        Thread.sleep(500)
        value
    }
            .subscribeOn(computation())

    override fun store(query: CurrentWeatherRepository.Query, data: CurrentWeather): Completable = Completable.fromAction {
        Thread.sleep(500)
        value = data
    }
            .subscribeOn(computation())

    override fun clear(query: CurrentWeatherRepository.Query?): Completable = Completable.fromAction {
        Thread.sleep(500)
        value = null
    }
            .subscribeOn(computation())
}