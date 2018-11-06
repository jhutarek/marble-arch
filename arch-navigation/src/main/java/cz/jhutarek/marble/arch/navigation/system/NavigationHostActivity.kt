package cz.jhutarek.marble.arch.navigation.system

import androidx.annotation.CallSuper
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import cz.jhutarek.marble.arch.log.infrastructure.logD
import cz.jhutarek.marble.arch.mvvm.model.State
import cz.jhutarek.marble.arch.mvvm.presentation.ViewModel
import cz.jhutarek.marble.arch.mvvm.system.MarbleActivity
import cz.jhutarek.marble.arch.navigation.device.AndroidNavigationController
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class NavigationHostActivity<M : ViewModel<S>, S : State> : MarbleActivity<M, S>() {

    @Inject protected lateinit var navigationController: AndroidNavigationController
    protected abstract val navigationHostResId: Int
    private var destinationsDisposable: Disposable? = null

    final override fun onSupportNavigateUp() = findNavController(navigationHostResId).navigateUp()

    @CallSuper
    override fun onStart() {
        super.onStart()

        logD("Bind navigation destinations")
        destinationsDisposable = navigationController.observe().subscribe {
            Navigation.findNavController(this@NavigationHostActivity, navigationHostResId).navigate(it)
        }
    }

    @CallSuper
    override fun onStop() {
        super.onStop()

        logD("Unbind navigation destinations")
        destinationsDisposable?.dispose()
        destinationsDisposable = null
    }
}