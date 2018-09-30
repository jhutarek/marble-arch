package cz.jhutarek.marble.arch.repository.data

import io.reactivex.Maybe

interface Source<in Q : Any, D : Any> {

    fun request(query: Q): Maybe<D>
}