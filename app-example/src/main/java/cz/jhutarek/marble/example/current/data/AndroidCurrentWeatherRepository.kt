package cz.jhutarek.marble.example.current.data

import cz.jhutarek.marble.arch.repository.data.BaseRepository
import cz.jhutarek.marble.example.current.domain.CurrentWeatherRepository
import cz.jhutarek.marble.example.current.model.CurrentWeather
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidCurrentWeatherRepository @Inject constructor(
    source: CurrentWeatherSource,
    cache: CurrentWeatherMemoryCache
) : BaseRepository<CurrentWeatherRepository.Query, CurrentWeather>(
    source,
    cache
), CurrentWeatherRepository