package cz.jhutarek.marble.arch.repository.data

import io.reactivex.Completable

// TODO support different keys
interface Cache<D : Any> : Source<D> {

    fun store(data: D): Completable

    fun clear(): Completable
}