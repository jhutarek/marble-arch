package cz.jhutarek.marble.example.current.di

import cz.jhutarek.marble.example.current.data.AndroidCurrentWeatherRepository
import cz.jhutarek.marble.example.current.domain.CurrentWeatherRepository
import cz.jhutarek.marble.example.current.system.CurrentWeatherFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CurrentWeatherModule {

    @ContributesAndroidInjector
    abstract fun currentWeatherFragment(): CurrentWeatherFragment

    @Binds
    abstract fun bindCurrentWeatherRepository(androidCurrentWeatherRepository: AndroidCurrentWeatherRepository): CurrentWeatherRepository
}