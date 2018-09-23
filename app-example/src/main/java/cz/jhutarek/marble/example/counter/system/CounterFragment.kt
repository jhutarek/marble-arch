package cz.jhutarek.marble.example.counter.system

import android.os.Bundle
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import cz.jhutarek.marble.arch.mvvm.system.MarbleFragment
import cz.jhutarek.marble.example.counter.presentation.CounterViewModel
import cz.jhutarek.marble.example.main.system.MainApplication
import cz.jhutarek.marblearch.R
import io.reactivex.Observable
import kotlinx.android.synthetic.main.counter_fragment.*

class CounterFragment : MarbleFragment<CounterViewModel, CounterViewModel.State>() {

    override val layoutResId = R.layout.counter_fragment

    // TODO use Android Dagger, move to base class
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainApplication.getInjector(context).inject(this)
    }

    override fun onBindStates(states: Observable<CounterViewModel.State>) = states.subscribeForViews {
        textInput.setText(it.counter.toString())
    }

    override fun onBindViews() {
        decreaseButton.clicks().subscribeForViewModel { decrease() }
        increaseButton.clicks().subscribeForViewModel { increase() }
        textInput.textChanges().subscribeForViewModel { setValue(it.toString()) }
    }
}