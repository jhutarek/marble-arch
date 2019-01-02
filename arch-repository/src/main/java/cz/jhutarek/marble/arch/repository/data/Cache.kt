package cz.jhutarek.marble.arch.repository.data

import io.reactivex.Completable

@Deprecated("Do not use, will be changed or removed")
interface Cache<in Q : Any, D : Any> : Source<Q, D> {

    fun store(query: Q, data: D): Completable

    fun clear(query: Q? = null): Completable
}