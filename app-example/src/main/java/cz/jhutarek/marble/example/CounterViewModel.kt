package cz.jhutarek.marble.example

import cz.jhutarek.marble.arch.mvvm.presentation.MarbleViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CounterViewModel @Inject constructor() : MarbleViewModel<CounterViewModel.State>(State()) {

    data class State(val counter: Int = 0) : cz.jhutarek.marble.arch.mvvm.model.State

    fun increase() {
        update { it.copy(counter = it.counter + 1) }
    }

    fun decrease() {
        update { it.copy(counter = it.counter - 1) }
    }
}