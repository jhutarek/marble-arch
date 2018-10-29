package cz.jhutarek.marble.example.settings.presentation

import cz.jhutarek.marble.arch.mvvm.presentation.ViewModel
import javax.inject.Inject
import javax.inject.Singleton
import cz.jhutarek.marble.arch.mvvm.model.State as MarbleState

@Singleton
class SettingsViewModel @Inject constructor() : ViewModel<SettingsViewModel.State>(State()) {

    class State : MarbleState // TODO
}