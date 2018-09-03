package cz.jhutarek.marble.arch.mvvm.presentation

import cz.jhutarek.marble.arch.mvvm.model.State
import io.reactivex.Observable

interface MarbleViewModel<S : State> {

    val states: Observable<S>
}