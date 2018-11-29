package cz.jhutarek.marble.example.main.system

import cz.jhutarek.marble.arch.mvvm.system.MarbleActivity
import cz.jhutarek.marble.arch.navigation.system.NavigationActivityDelegate
import cz.jhutarek.marble.example.main.presentation.MainViewModel
import cz.jhutarek.marblearch.R
import javax.inject.Inject

class MainActivity : MarbleActivity<MainViewModel, MainViewModel.State>() {

    @Inject internal lateinit var navigationDelegate: NavigationActivityDelegate

    override val layoutResId = R.layout.main__main_activity

    override fun onStart() {
        super.onStart()

        navigationDelegate.onStart(this)
    }

    override fun onStop() {
        super.onStop()

        navigationDelegate.onStop(this)
    }

    override fun onSupportNavigateUp() = navigationDelegate.onSupportNavigateUp(this)
}
