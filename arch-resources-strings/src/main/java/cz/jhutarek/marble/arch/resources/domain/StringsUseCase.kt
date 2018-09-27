package cz.jhutarek.marble.arch.resources.domain

import cz.jhutarek.marble.arch.usecase.domain.UseCase
import javax.inject.Inject
import javax.inject.Singleton

sealed class StringsUseCase<in I, out O> : UseCase<I, O> {

    // TODO test
    @Singleton
    class GetString @Inject constructor(
            private val controller: StringsController
    ) : StringsUseCase<GetString.Params, String>() {
        data class Params(val resId: Int, val formatArgs: List<Any>)

        override operator fun invoke(input: Params): String = controller.getString(input.resId, input.formatArgs)

        operator fun invoke(resId: Int) = invoke(Params(resId, listOf()))

        operator fun invoke(resId: Int, vararg formatArgs: Any) = invoke(Params(resId, formatArgs.toList()))
    }
}