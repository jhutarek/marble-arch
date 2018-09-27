package cz.jhutarek.marble.example.overview.system

import android.os.Bundle
import cz.jhutarek.marble.arch.mvvm.system.MarbleFragment
import cz.jhutarek.marble.example.main.system.MainApplication
import cz.jhutarek.marble.example.overview.presentation.OverviewViewModel
import cz.jhutarek.marblearch.R

class OverviewFragment : MarbleFragment<OverviewViewModel, OverviewViewModel.State>() {

    override val layoutResId = R.layout.overview__overview_fragment

    // TODO use Android Dagger, move to base class
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainApplication.getInjector(context).inject(this)
    }
}