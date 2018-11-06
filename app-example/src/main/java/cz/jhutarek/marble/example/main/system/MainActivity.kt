package cz.jhutarek.marble.example.main.system

import cz.jhutarek.marble.arch.mvvm.system.MarbleActivity
import cz.jhutarek.marble.example.main.presentation.MainViewModel
import cz.jhutarek.marble.example.main.system.MainApplication.Injector
import cz.jhutarek.marblearch.R

class MainActivity : MarbleActivity<MainViewModel, MainViewModel.State>() {

    override val layoutResId = R.layout.main__main_activity

    override fun onInjection() {
        Injector(this).inject(this)
    }
}
