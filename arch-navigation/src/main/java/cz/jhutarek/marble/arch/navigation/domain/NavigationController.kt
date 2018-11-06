package cz.jhutarek.marble.arch.navigation.domain

import io.reactivex.Observable

interface NavigationController {

    fun observe(): Observable<Int>

    fun navigateTo(destination: Int)
}