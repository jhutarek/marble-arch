package cz.jhutarek.marble.example.current.di

import cz.jhutarek.marble.example.current.data.AndroidCurrentWeatherRepository
import cz.jhutarek.marble.example.current.domain.CurrentWeatherRepository
import dagger.Binds
import dagger.Module

@Module
abstract class CurrentWeatherModule {

    @Binds
    abstract fun bindCurrentWeatherRepository(androidCurrentWeatherRepository: AndroidCurrentWeatherRepository): CurrentWeatherRepository
}