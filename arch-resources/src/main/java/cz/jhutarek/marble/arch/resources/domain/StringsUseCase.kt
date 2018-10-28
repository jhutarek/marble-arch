package cz.jhutarek.marble.arch.resources.domain

import cz.jhutarek.marble.arch.usecase.domain.UseCase
import javax.inject.Inject
import javax.inject.Singleton

sealed class StringsUseCase<in I, out O> : UseCase<I, O> {

    @Singleton
    class GetString @Inject constructor(private val controller: StringsController) : StringsUseCase<Int, String>() {

        override operator fun invoke(input: Int) = controller.getString(input)
    }
}