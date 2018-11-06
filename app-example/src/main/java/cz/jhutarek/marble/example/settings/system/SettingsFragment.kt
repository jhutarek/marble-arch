package cz.jhutarek.marble.example.settings.system

import cz.jhutarek.marble.arch.mvvm.system.MarbleFragment
import cz.jhutarek.marble.example.main.system.MainApplication.Injector
import cz.jhutarek.marble.example.settings.presentation.SettingsViewModel
import cz.jhutarek.marblearch.R

class SettingsFragment : MarbleFragment<SettingsViewModel, SettingsViewModel.State>() {

    override val layoutResId = R.layout.settings__settings_fragment

    override fun onInjection() {
        Injector(context).inject(this)
    }
}