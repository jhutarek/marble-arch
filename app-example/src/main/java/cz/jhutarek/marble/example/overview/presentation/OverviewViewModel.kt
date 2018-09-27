package cz.jhutarek.marble.example.overview.presentation

import cz.jhutarek.marble.arch.mvvm.presentation.ViewModel
import javax.inject.Inject
import javax.inject.Singleton
import cz.jhutarek.marble.arch.mvvm.model.State as MarbleState

@Singleton
class OverviewViewModel @Inject constructor() : ViewModel<OverviewViewModel.State>(State()) {

    data class State(val tmp: String = "") : MarbleState
}