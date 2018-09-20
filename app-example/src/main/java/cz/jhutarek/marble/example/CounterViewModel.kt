package cz.jhutarek.marble.example

import cz.jhutarek.marble.arch.mvvm.presentation.ViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CounterViewModel @Inject constructor() : ViewModel<CounterViewModel.State>(State()) {

    data class State(val counter: Int = 0) : cz.jhutarek.marble.arch.mvvm.model.State

    fun setValue(value: String) = statesRelay.accept { it.copy(counter = value.toIntOrNull() ?: 0) }

    fun increase() = statesRelay.accept { it.copy(counter = it.counter + 1) }

    fun decrease() = statesRelay.accept { it.copy(counter = it.counter - 1) }
}