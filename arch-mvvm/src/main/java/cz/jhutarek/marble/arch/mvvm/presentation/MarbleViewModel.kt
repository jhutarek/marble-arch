package cz.jhutarek.marble.arch.mvvm.presentation

import com.jakewharton.rxrelay2.BehaviorRelay
import cz.jhutarek.marble.arch.mvvm.model.MarbleState
import io.reactivex.Observable

// TODO test
abstract class MarbleViewModel<S : MarbleState>(defaultState: S) {

    private val statesRelay: BehaviorRelay<S> = BehaviorRelay.createDefault(defaultState)
    val states: Observable<S> = statesRelay.hide()

    protected fun updateState(updater: (S) -> S) {
        statesRelay.accept(updater(statesRelay.value))
    }
}