package cz.jhutarek.marble.arch.repository.model

sealed class Data<out Q : Any, out T : Any> {

    abstract val query: Q

    abstract fun <R : Any> mapQuery(mapper: (Q) -> R): Data<R, T>

    // TODO test
    data class Loading<out Q : Any>(override val query: Q) : Data<Q, Nothing>() {
        override fun <R : Any> mapQuery(mapper: (Q) -> R) = Loading(mapper(query))
    }

    // TODO test
    data class Empty<out Q : Any>(override val query: Q) : Data<Q, Nothing>() {
        override fun <R : Any> mapQuery(mapper: (Q) -> R) = Empty(mapper(query))
    }

    // TODO test
    data class Loaded<out Q : Any, out T : Any>(override val query: Q, val value: T) : Data<Q, T>() {
        override fun <R : Any> mapQuery(mapper: (Q) -> R) = Loaded(mapper(query), value)
    }

    // TODO test
    data class Error<out Q : Any>(override val query: Q, val error: Throwable) : Data<Q, Nothing>() {
        override fun <R : Any> mapQuery(mapper: (Q) -> R) = Error(mapper(query), error)
    }
}