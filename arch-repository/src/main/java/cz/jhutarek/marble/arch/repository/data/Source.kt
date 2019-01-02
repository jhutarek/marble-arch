package cz.jhutarek.marble.arch.repository.data

import io.reactivex.Maybe

@Deprecated("Do not use, will be changed or removed")
interface Source<in Q : Any, D : Any> {

    fun request(query: Q): Maybe<D>
}