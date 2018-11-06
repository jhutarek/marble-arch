package cz.jhutarek.marble.example.main.presentation

import cz.jhutarek.marble.arch.mvvm.presentation.ViewModel
import javax.inject.Inject
import javax.inject.Singleton
import cz.jhutarek.marble.arch.mvvm.model.State as MarbleState

@Singleton
class MainViewModel @Inject constructor() : ViewModel<MainViewModel.State>(State()) {

    class State : MarbleState
}