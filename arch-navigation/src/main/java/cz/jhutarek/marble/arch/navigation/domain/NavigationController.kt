package cz.jhutarek.marble.arch.navigation.domain

import cz.jhutarek.marble.arch.navigation.model.Destination
import io.reactivex.Observable

interface NavigationController {

    fun observe(): Observable<Int>

    fun navigate(destination: Destination)
}