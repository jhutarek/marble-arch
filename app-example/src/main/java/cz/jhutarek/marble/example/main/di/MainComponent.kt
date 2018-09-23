package cz.jhutarek.marble.example.main.di

import cz.jhutarek.marble.example.counter.di.CounterComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface MainComponent : CounterComponent