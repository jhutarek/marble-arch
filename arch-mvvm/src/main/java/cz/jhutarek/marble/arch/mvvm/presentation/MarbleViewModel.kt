package cz.jhutarek.marble.arch.mvvm.presentation

import com.jakewharton.rxrelay2.BehaviorRelay
import cz.jhutarek.marble.arch.mvvm.model.State
import io.reactivex.Observable

// TODO test
abstract class MarbleViewModel<S : State>(private val defaultState: S) {

    private val statesRelay: BehaviorRelay<S> = BehaviorRelay.createDefault(defaultState)
    val states: Observable<S> = statesRelay.hide()

    protected fun update(updater: (S) -> S) {
        statesRelay.accept(updater(statesRelay.value))
    }
}