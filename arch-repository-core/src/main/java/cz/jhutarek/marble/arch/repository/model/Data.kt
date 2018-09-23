package cz.jhutarek.marble.arch.repository.model

sealed class Data<out K : Any, out T : Any> {

    abstract val key: K

    data class Loading<out K : Any>(override val key: K) : Data<K, Nothing>()

    data class Empty<out K : Any>(override val key: K) : Data<K, Nothing>()

    data class Loaded<out K : Any, out T : Any>(override val key: K, val value: T) : Data<K, T>()

    data class Error<out K : Any>(override val key: K, val error: Throwable) : Data<K, Nothing>()
}