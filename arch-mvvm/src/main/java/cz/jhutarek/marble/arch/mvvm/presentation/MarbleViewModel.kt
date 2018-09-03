package cz.jhutarek.marble.arch.mvvm.presentation

import com.jakewharton.rxrelay2.BehaviorRelay
import cz.jhutarek.marble.arch.mvvm.model.State
import io.reactivex.Observable

// TODO test
abstract class MarbleViewModel<S : State> {

    private val statesRelay: BehaviorRelay<S> by lazy { BehaviorRelay.createDefault(defaultState) } // TODO is this 'lazy' trick OK?
    val states: Observable<S> = statesRelay.hide()
    protected abstract val defaultState: S

    protected fun update(updater: (S) -> S) {
        statesRelay.accept(updater(statesRelay.value))
    }
}