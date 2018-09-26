package cz.jhutarek.marble.arch.repository.model

sealed class Data<out Q : Any, out T : Any> {

    abstract val query: Q

    data class Loading<out Q : Any>(override val query: Q) : Data<Q, Nothing>()

    data class Empty<out Q : Any>(override val query: Q) : Data<Q, Nothing>()

    data class Loaded<out Q : Any, out T : Any>(override val query: Q, val value: T) : Data<Q, T>()

    data class Error<out Q : Any>(override val query: Q, val error: Throwable) : Data<Q, Nothing>()
}