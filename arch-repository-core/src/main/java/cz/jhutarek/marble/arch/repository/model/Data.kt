package cz.jhutarek.marble.arch.repository.model

sealed class Data<out T : Any> {
    object Empty : Data<Nothing>() {
        override fun toString(): String = "Empty"
    }

    data class Loaded<out T : Any>(val value: T) : Data<T>()

    data class Error(val error: Throwable) : Data<Nothing>()
}