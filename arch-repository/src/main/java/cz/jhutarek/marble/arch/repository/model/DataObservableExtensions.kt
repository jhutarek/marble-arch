package cz.jhutarek.marble.arch.repository.model

import io.reactivex.Observable

// TODO test
@Deprecated("Do not use, will be changed or removed")
fun <Q : Any, T : Any, R : Any> Observable<LegacyData<Q, T>>.mapQuery(mapper: (Q) -> R): Observable<LegacyData<R, T>> = map { it.mapQuery(mapper) }