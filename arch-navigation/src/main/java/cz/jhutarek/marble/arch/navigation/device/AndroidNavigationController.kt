package cz.jhutarek.marble.arch.navigation.device

import com.jakewharton.rxrelay2.PublishRelay
import cz.jhutarek.marble.arch.log.infrastructure.logD
import cz.jhutarek.marble.arch.log.infrastructure.logI
import cz.jhutarek.marble.arch.navigation.domain.NavigationController
import cz.jhutarek.marble.arch.navigation.model.Destination
import io.reactivex.Observable
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidNavigationController @Inject constructor(
    /**
     * Android NavController invokes its listener for every popped destination, however we don't want to listen to each of those changes
     * (we want just the "final", settled destination).
     * This delay is used to fine-tune the debounce limit after which we proclaim the destination to be settled.
     * Setting it too high will result in visible UI delay, setting it too low might cause unwanted emissions.
     */
    private val destinationsDebounceLimitMillis: Long = 20
) : NavigationController {

    private val destinationRequestsRelay = PublishRelay.create<Destination>()
    private val observeRelay = PublishRelay.create<Int>()

    fun observeDestinationRequests(): Observable<Destination> = destinationRequestsRelay.hide()
    override fun observe(): Observable<Int> = observeRelay
        .hide()
        .debounce(destinationsDebounceLimitMillis, MILLISECONDS)

    override fun navigate(destination: Destination) {
        logI("Navigate to: $destination")

        destinationRequestsRelay.accept(destination)
    }

    fun notifyNavigationExecuted(destinationId: Int) {
        logD("Notify navigation executed: $destinationId")

        observeRelay.accept(destinationId)
    }
}