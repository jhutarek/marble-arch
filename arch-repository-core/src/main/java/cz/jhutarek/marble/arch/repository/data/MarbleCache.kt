package cz.jhutarek.marble.arch.repository.data

import io.reactivex.Completable

// TODO support different keys
interface MarbleCache<D : Any> : MarbleSource<D> {

    fun store(data: D): Completable

    fun clear(): Completable
}