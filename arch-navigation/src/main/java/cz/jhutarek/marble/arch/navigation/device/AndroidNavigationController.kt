package cz.jhutarek.marble.arch.navigation.device

import com.jakewharton.rxrelay2.PublishRelay
import cz.jhutarek.marble.arch.log.infrastructure.logI
import cz.jhutarek.marble.arch.navigation.domain.NavigationController
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidNavigationController @Inject constructor() : NavigationController {

    private val destinationsRelay = PublishRelay.create<Int>()

    fun observe(): Observable<Int> = destinationsRelay.hide()

    override fun navigateTo(destination: Int) {
        logI("Navigate to: $destination")

        destinationsRelay.accept(destination)
    }
}