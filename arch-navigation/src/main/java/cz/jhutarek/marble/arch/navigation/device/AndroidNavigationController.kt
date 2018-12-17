package cz.jhutarek.marble.arch.navigation.device

import com.jakewharton.rxrelay2.BehaviorRelay
import cz.jhutarek.marble.arch.log.infrastructure.logD
import cz.jhutarek.marble.arch.log.infrastructure.logI
import cz.jhutarek.marble.arch.navigation.domain.NavigationController
import cz.jhutarek.marble.arch.navigation.model.Destination
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidNavigationController @Inject constructor() : NavigationController {

    private val destinationRequestsRelay = BehaviorRelay.create<Destination>()
    private val observeRelay = BehaviorRelay.create<Int>()

    fun observeDestinationRequests(): Observable<Destination> = destinationRequestsRelay.hide()
    override fun observe(): Observable<Int> = observeRelay.hide()

    override fun navigate(destination: Destination) {
        logI("Navigate to: $destination")

        destinationRequestsRelay.accept(destination)
    }

    fun notifyNavigationExecuted(destinationId: Int) {
        logD("Notify navigation executed: $destinationId")

        observeRelay.accept(destinationId)
    }
}