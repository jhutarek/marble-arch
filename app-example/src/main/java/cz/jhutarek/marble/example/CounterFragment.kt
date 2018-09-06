package cz.jhutarek.marble.example

import android.os.Bundle
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import cz.jhutarek.marble.arch.mvvm.ui.MarbleFragment
import cz.jhutarek.marblearch.R
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.counter_fragment.*

class CounterFragment : MarbleFragment<CounterViewModel, CounterViewModel.State>() {

    override val layoutResId = R.layout.counter_fragment

    // TODO use Android Dagger, move to base class
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainApplication.getInjector(context).inject(this)
    }

    override fun onBindViews(viewModel: CounterViewModel) {
        decrease.clicks().safe().subscribe { viewModel.decrease() }
        increase.clicks().safe().subscribe { viewModel.increase() }
        counter.textChanges().safe().subscribe { viewModel.setValue(it.toString()) }
    }

    override fun onBindStates(states: Observable<CounterViewModel.State>): Disposable = states.safeSubscribe {
        counter.setText(it.counter.toString())
    }
}