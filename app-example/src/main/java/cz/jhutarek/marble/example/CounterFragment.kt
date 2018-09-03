package cz.jhutarek.marble.example

import android.os.Bundle
import android.view.View
import cz.jhutarek.marble.arch.mvvm.ui.MarbleFragment
import cz.jhutarek.marblearch.R
import kotlinx.android.synthetic.main.counter_fragment.*

class CounterFragment : MarbleFragment<CounterViewModel, CounterViewModel.State>() {

    override val layoutResId = R.layout.counter_fragment

    override fun renderState(state: CounterViewModel.State) {
        counter.text = state.counter.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainApplication.getInjector(context).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        decrease.setOnClickListener { viewModel.decrease() }
        increase.setOnClickListener { viewModel.increase() }
    }
}