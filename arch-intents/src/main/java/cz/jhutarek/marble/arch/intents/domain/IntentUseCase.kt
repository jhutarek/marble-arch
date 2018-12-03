package cz.jhutarek.marble.arch.intents.domain

import cz.jhutarek.marble.arch.usecase.domain.UseCase
import javax.inject.Inject
import javax.inject.Singleton

sealed class IntentUseCase<in I, out O> : UseCase<I, O> {

    @Singleton
    class Browse @Inject constructor(private val intentController: IntentController) : IntentUseCase<String, Unit>() {
        override fun invoke(input: String) = intentController.browse(input)
    }
}

