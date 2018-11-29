package cz.jhutarek.marble.arch.navigation.device

import com.jakewharton.rxrelay2.PublishRelay
import cz.jhutarek.marble.arch.log.infrastructure.logD
import cz.jhutarek.marble.arch.log.infrastructure.logI
import cz.jhutarek.marble.arch.navigation.domain.NavigationController
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidNavigationController @Inject constructor() : NavigationController {

    private val destinationRequestsRelay = PublishRelay.create<Int>()
    private val observeRelay = PublishRelay.create<Int>()

    fun observeDestinationRequests(): Observable<Int> = destinationRequestsRelay.hide()
    override fun observe(): Observable<Int> = observeRelay.hide()

    override fun navigate(destination: Int) {
        logI("Navigate to: $destination")

        destinationRequestsRelay.accept(destination)
    }

    fun notifyNavigationExecuted(destination: Int) {
        logD("Notify navigation executed: $destination")

        observeRelay.accept(destination)
    }
}