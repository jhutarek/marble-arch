package cz.jhutarek.marble.example

import cz.jhutarek.marble.arch.mvvm.model.MarbleState
import cz.jhutarek.marble.arch.mvvm.presentation.MarbleViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CounterViewModel @Inject constructor() : MarbleViewModel<CounterViewModel.State>(State()) {

    data class State(val counter: Int = 0) : MarbleState

    fun setValue(value: String) = updateState { it.copy(counter = value.toIntOrNull() ?: 0) }

    fun increase() = updateState { it.copy(counter = it.counter + 1) }

    fun decrease() = updateState { it.copy(counter = it.counter - 1) }
}