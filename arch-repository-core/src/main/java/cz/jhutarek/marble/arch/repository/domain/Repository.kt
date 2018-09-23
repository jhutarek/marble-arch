package cz.jhutarek.marble.arch.repository.domain

import cz.jhutarek.marble.arch.repository.model.Data
import io.reactivex.Completable
import io.reactivex.Observable

interface Repository<K : Any, D : Any> {

    fun observe(): Observable<Data<K, D>>

    fun request(key: K)

    fun clearCaches(key: K): Completable
}