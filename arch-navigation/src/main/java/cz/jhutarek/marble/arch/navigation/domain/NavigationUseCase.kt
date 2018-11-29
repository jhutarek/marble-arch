package cz.jhutarek.marble.arch.navigation.domain

import cz.jhutarek.marble.arch.usecase.domain.UseCase
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

sealed class NavigationUseCase<in I, out O> : UseCase<I, O> {

    @Singleton
    class Navigate @Inject constructor(private val controller: NavigationController) : NavigationUseCase<Int, Unit>() {
        override fun invoke(input: Int) = controller.navigate(input)
    }

    @Singleton
    class Observe @Inject constructor(private val controller: NavigationController) : NavigationUseCase<Unit, Observable<Int>>() {
        override fun invoke(input: Unit): Observable<Int> = controller.observe()
    }
}