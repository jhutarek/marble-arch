package cz.jhutarek.marble.arch.keyboard.domain

import cz.jhutarek.marble.arch.usecase.domain.UseCase
import javax.inject.Inject
import javax.inject.Singleton

sealed class KeyboardUseCase<in I, out O> : UseCase<I, O> {

    @Singleton
    class HideKeyboard @Inject constructor(private val keyboardController: KeyboardController) : KeyboardUseCase<Unit, Unit>() {
        override fun invoke(input: Unit) {
            keyboardController.hideKeyboard()
        }
    }
}