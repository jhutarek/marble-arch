package cz.jhutarek.marble.arch.repository.domain

import cz.jhutarek.marble.arch.repository.model.Data
import io.reactivex.Completable
import io.reactivex.Observable

interface Repository<Q : Any, D : Any> {

    fun observe(): Observable<Data<Q, D>>

    fun load(query: Q)

    fun clearCaches(query: Q? = null): Completable
}