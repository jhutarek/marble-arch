package cz.jhutarek.marble.arch.repository.domain

import cz.jhutarek.marble.arch.repository.model.Data
import io.reactivex.Observable

// TODO support different keys
interface Repository<D : Any> {

    fun observe(): Observable<Data<D>>

    fun request()
}