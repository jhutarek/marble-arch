package cz.jhutarek.marble.example.counter.di

import cz.jhutarek.marble.example.counter.system.CounterFragment

interface CounterComponent {
    fun inject(fragment: CounterFragment)
}