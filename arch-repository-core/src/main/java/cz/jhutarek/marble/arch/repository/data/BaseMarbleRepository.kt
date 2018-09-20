package cz.jhutarek.marble.arch.repository.data

import cz.jhutarek.marble.arch.repository.domain.MarbleRepository
import io.reactivex.Observable

// TODO support different keys
// TODO test
abstract class BaseMarbleRepository<D : Any>(
        private val source: MarbleSource<D>,
        private vararg val caches: MarbleCache<D>
) : MarbleRepository<D> {

    final override fun observe(): Observable<D> {
        TODO()
    }

    final override fun request() {
        TODO()
    }
}