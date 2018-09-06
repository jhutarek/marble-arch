package cz.jhutarek.marble.arch.mvvm.presentation

import com.jakewharton.rxrelay2.BehaviorRelay
import cz.jhutarek.marble.arch.mvvm.model.MarbleState
import io.reactivex.Observable

// TODO test
abstract class MarbleViewModel<S : MarbleState>(defaultState: S) {

    protected val statesRelay: BehaviorRelay<S> = BehaviorRelay.createDefault(defaultState)
    val states: Observable<S> = statesRelay.hide()

    protected fun BehaviorRelay<S>.accept(updater: S.() -> S) {
        accept(updater(value))
    }
}