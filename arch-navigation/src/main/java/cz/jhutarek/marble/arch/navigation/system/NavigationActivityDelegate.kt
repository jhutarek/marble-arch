package cz.jhutarek.marble.arch.navigation.system

import android.app.Activity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import cz.jhutarek.marble.arch.log.infrastructure.logD
import cz.jhutarek.marble.arch.navigation.device.AndroidNavigationController
import cz.jhutarek.marble.arch.navigation.model.Destination.Type.ADD_TO_TOP
import cz.jhutarek.marble.arch.navigation.model.Destination.Type.POP_TO_PREVIOUS
import io.reactivex.disposables.Disposable
import javax.inject.Inject
import javax.inject.Singleton

// TODO test
@Singleton
class NavigationActivityDelegate @Inject constructor(
    private val navigationController: AndroidNavigationController,
    private val navigationHostResId: Int
) {
    private var destinationsDisposable: Disposable? = null
    private var systemNavigationListener: NavController.OnNavigatedListener? = null

    fun onSupportNavigateUp(activity: Activity) = activity.findNavController(navigationHostResId).navigateUp()

    fun onStart(activity: Activity) {
        logD("Bind navigation destinations")

        val systemNavigationController = activity.findNavController(navigationHostResId)

        destinationsDisposable = navigationController.observeDestinationRequests().subscribe {
            when (it.type) {
                ADD_TO_TOP -> systemNavigationController.navigate(it.id)
                POP_TO_PREVIOUS -> systemNavigationController.navigate(
                    it.id,
                    Bundle.EMPTY,
                    NavOptions.Builder()
                        .setPopUpTo(it.id, true)
                        .build()
                )
            }
        }
        NavController.OnNavigatedListener { _, destination -> navigationController.notifyNavigationExecuted(destination.id) }
            .also { systemNavigationListener = it }
            .let { systemNavigationController.addOnNavigatedListener(it) }
    }

    fun onStop(activity: Activity) {
        logD("Unbind navigation destinations")

        destinationsDisposable?.dispose()
        destinationsDisposable = null

        systemNavigationListener?.let { activity.findNavController(navigationHostResId).removeOnNavigatedListener(it) }
        systemNavigationListener = null
    }
}