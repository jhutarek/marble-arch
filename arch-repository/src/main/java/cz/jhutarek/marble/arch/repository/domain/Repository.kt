package cz.jhutarek.marble.arch.repository.domain

import cz.jhutarek.marble.arch.repository.model.LegacyData
import io.reactivex.Completable
import io.reactivex.Observable

@Deprecated("Do not use, will be changed or removed")
interface Repository<Q : Any, D : Any> {

    fun observe(): Observable<LegacyData<Q, D>>

    fun load(query: Q) // TODO return Completable

    fun update(query: Q) // TODO return Completable

    fun clearCaches(query: Q? = null): Completable
}