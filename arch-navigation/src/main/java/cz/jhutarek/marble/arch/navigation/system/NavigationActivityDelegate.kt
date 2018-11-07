package cz.jhutarek.marble.arch.navigation.system

import android.app.Activity
import androidx.navigation.findNavController
import cz.jhutarek.marble.arch.log.infrastructure.logD
import cz.jhutarek.marble.arch.navigation.device.AndroidNavigationController
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

    fun onSupportNavigateUp(activity: Activity) = activity.findNavController(navigationHostResId).navigateUp()

    fun onStart(activity: Activity) {
        logD("Bind navigation destinations")

        destinationsDisposable = navigationController.observe().subscribe {
            activity.findNavController(navigationHostResId).navigate(it)
        }
    }

    fun onStop() {
        logD("Unbind navigation destinations")

        destinationsDisposable?.dispose()
        destinationsDisposable = null
    }
}