package cz.jhutarek.marble.arch.repository.domain

import io.reactivex.Observable

// TODO support different keys
interface MarbleRepository<D : Any> {

    fun observe(): Observable<D>

    fun request()
}