package cz.jhutarek.marble.example.overview.system

import android.os.Bundle
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import cz.jhutarek.marble.arch.mvvm.system.MarbleFragment
import cz.jhutarek.marble.arch.mvvm.system.textString
import cz.jhutarek.marble.example.main.system.MainApplication
import cz.jhutarek.marble.example.overview.presentation.OverviewViewModel
import cz.jhutarek.marblearch.R
import io.reactivex.Observable
import kotlinx.android.synthetic.main.overview__overview_fragment.*
import java.util.concurrent.TimeUnit.MILLISECONDS

class OverviewFragment : MarbleFragment<OverviewViewModel, OverviewViewModel.State>() {

    override val layoutResId = R.layout.overview__overview_fragment

    // TODO use Android Dagger, move to base class
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainApplication.getInjector(context).inject(this)
    }

    override fun onInitializeViews() {
        toolbar.inflateMenu(R.menu.overview__overview_menu)
    }

    override fun onBindViews() {
        toolbar.menu.findItem(R.id.refresh).clicks().subscribeForViewModel { refresh() }
        input.textChanges().debounce(250, MILLISECONDS).subscribeForViewModel { setInput(it) }
    }

    override fun onBindStates(states: Observable<OverviewViewModel.State>) = states.subscribeForViews {
        toolbar.menu.findItem(R.id.refresh).isEnabled = it.refreshEnabled
        input.textString = it.input
    }
}