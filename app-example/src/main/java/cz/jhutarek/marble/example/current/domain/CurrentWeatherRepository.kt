package cz.jhutarek.marble.example.current.domain

import cz.jhutarek.marble.arch.repository.domain.Repository
import cz.jhutarek.marble.example.current.model.CurrentWeather

interface CurrentWeatherRepository : Repository<CurrentWeatherRepository.Query, CurrentWeather> {
    class Query
}