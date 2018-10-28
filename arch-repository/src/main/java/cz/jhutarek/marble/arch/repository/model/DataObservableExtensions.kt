package cz.jhutarek.marble.arch.repository.model

import io.reactivex.Observable

// TODO test
fun <Q : Any, T : Any, R : Any> Observable<Data<Q, T>>.mapQuery(mapper: (Q) -> R): Observable<Data<R, T>> = map { it.mapQuery(mapper) }