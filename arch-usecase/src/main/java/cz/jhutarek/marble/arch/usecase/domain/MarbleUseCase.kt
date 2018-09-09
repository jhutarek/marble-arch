package cz.jhutarek.marble.arch.usecase.domain

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

sealed class MarbleUseCase<in I, out O> {

    abstract fun execute(input: I): O

    abstract class ObservableUseCase<in I, O> : MarbleUseCase<I, Observable<O>>()

    abstract class SingleUseCase<in I, O> : MarbleUseCase<I, Single<O>>()

    abstract class MaybeUseCase<in I, O> : MarbleUseCase<I, Maybe<O>>()

    abstract class CompletableUseCase<in I> : MarbleUseCase<I, Completable>()
}

