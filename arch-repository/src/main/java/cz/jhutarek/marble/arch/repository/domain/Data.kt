package cz.jhutarek.marble.arch.repository.domain

sealed class Data<out T : Any> {

    object Loading : Data<Nothing>() {
        override fun toString() = "Loading"
    }

    object Empty : Data<Nothing>() {
        override fun toString() = "Empty"
    }

    data class Loaded<out T : Any>(val value: T) : Data<T>()

    data class Error(val error: Throwable) : Data<Nothing>()
}