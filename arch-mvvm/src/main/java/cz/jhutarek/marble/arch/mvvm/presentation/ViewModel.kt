package cz.jhutarek.marble.arch.mvvm.presentation

import com.jakewharton.rxrelay2.BehaviorRelay
import cz.jhutarek.marble.arch.log.infrastructure.logI
import cz.jhutarek.marble.arch.mvvm.model.State
import io.reactivex.Observable

// TODO test
abstract class ViewModel<S : State>(defaultState: S) {

    protected val statesRelay: BehaviorRelay<S> = BehaviorRelay.createDefault(defaultState)
    val states: Observable<S> = statesRelay.hide().doOnNext { logI("$it") }

    protected fun BehaviorRelay<S>.accept(updater: (S) -> S) {
        accept(updater(value))
    }
}