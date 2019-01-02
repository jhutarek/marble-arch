package cz.jhutarek.marble.arch.repository.model

@Deprecated("Do not use, will be changed or removed")
sealed class LegacyData<out Q : Any, out T : Any> {

    abstract val query: Q

    abstract fun <R : Any> mapQuery(mapper: (Q) -> R): LegacyData<R, T>

    // TODO test
    data class Loading<out Q : Any>(override val query: Q) : LegacyData<Q, Nothing>() {
        override fun <R : Any> mapQuery(mapper: (Q) -> R) = Loading(mapper(query))
    }

    // TODO test
    data class Empty<out Q : Any>(override val query: Q) : LegacyData<Q, Nothing>() {
        override fun <R : Any> mapQuery(mapper: (Q) -> R) = Empty(mapper(query))
    }

    // TODO test
    data class Loaded<out Q : Any, out T : Any>(override val query: Q, val value: T) : LegacyData<Q, T>() {
        override fun <R : Any> mapQuery(mapper: (Q) -> R) = Loaded(mapper(query), value)
    }

    // TODO test
    data class Error<out Q : Any>(override val query: Q, val error: Throwable) : LegacyData<Q, Nothing>() {
        override fun <R : Any> mapQuery(mapper: (Q) -> R) = Error(mapper(query), error)
    }
}