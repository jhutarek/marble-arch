package cz.jhutarek.marble.arch.navigation.system

import androidx.navigation.findNavController
import cz.jhutarek.marble.arch.mvvm.model.State
import cz.jhutarek.marble.arch.mvvm.presentation.ViewModel
import cz.jhutarek.marble.arch.mvvm.system.MarbleActivity

abstract class NavigationHostActivity<M : ViewModel<S>, S : State> : MarbleActivity<M, S>() {

    protected abstract val navigationHostResId: Int

    final override fun onSupportNavigateUp() = findNavController(navigationHostResId).navigateUp()
}