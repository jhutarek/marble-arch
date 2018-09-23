package cz.jhutarek.marble.arch.repository.data

import io.reactivex.Completable

interface Cache<in K : Any, D : Any> : Source<K, D> {

    fun store(key: K, data: D): Completable

    fun clear(key: K): Completable
}