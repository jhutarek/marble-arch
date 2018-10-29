package cz.jhutarek.marble.example.settings.system

import android.os.Bundle
import cz.jhutarek.marble.arch.mvvm.system.MarbleFragment
import cz.jhutarek.marble.example.main.system.MainApplication
import cz.jhutarek.marble.example.settings.presentation.SettingsViewModel
import cz.jhutarek.marblearch.R

class SettingsFragment : MarbleFragment<SettingsViewModel, SettingsViewModel.State>() {

    override val layoutResId = R.layout.settings__settings_fragment

    // TODO use Android Dagger, move to base class
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainApplication.getInjector(context).inject(this)
    }
}