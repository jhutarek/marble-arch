package cz.jhutarek.marble.arch.usecase.domain

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

sealed class UseCase<in I, out O> {

    abstract fun execute(input: I): O

    abstract class ObservableUseCase<in I, O> : UseCase<I, Observable<O>>()

    abstract class SingleUseCase<in I, O> : UseCase<I, Single<O>>()

    abstract class MaybeUseCase<in I, O> : UseCase<I, Maybe<O>>()

    abstract class CompletableUseCase<in I> : UseCase<I, Completable>()
}

