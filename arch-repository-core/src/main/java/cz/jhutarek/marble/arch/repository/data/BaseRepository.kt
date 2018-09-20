package cz.jhutarek.marble.arch.repository.data

import cz.jhutarek.marble.arch.repository.domain.Repository
import cz.jhutarek.marble.arch.repository.model.Data
import io.reactivex.Observable

// TODO support different keys
// TODO test
abstract class BaseRepository<D : Any>(
        private val source: Source<D>,
        private vararg val caches: Cache<D>
) : Repository<D> {

    final override fun observe(): Observable<Data<D>> {
        TODO()
    }

    final override fun request() {
        TODO()
    }
}