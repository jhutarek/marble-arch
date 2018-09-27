package cz.jhutarek.marble.example.current.di

import cz.jhutarek.marble.example.current.system.CurrentWeatherFragment

interface CurrentWeatherComponent {
    fun inject(fragment: CurrentWeatherFragment)
}