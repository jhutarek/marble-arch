package cz.jhutarek.marble.arch.repository.data

import io.reactivex.Maybe

// TODO support different keys
interface MarbleSource<D : Any> {

    fun load(): Maybe<D>
}