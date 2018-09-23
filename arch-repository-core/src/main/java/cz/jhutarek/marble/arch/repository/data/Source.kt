package cz.jhutarek.marble.arch.repository.data

import io.reactivex.Maybe

interface Source<in K : Any, D : Any> {

    fun request(key: K): Maybe<D>
}