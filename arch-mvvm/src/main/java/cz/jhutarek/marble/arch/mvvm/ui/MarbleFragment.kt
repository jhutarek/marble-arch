package cz.jhutarek.marble.arch.mvvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import cz.jhutarek.marble.arch.mvvm.model.State
import cz.jhutarek.marble.arch.mvvm.presentation.MarbleViewModel
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import javax.inject.Inject

// TODO test
abstract class MarbleFragment<M : MarbleViewModel<S>, S : State> : Fragment() {

    @Inject protected lateinit var viewModel: M
    protected abstract val layoutResId: Int
    private var statesDisposable: Disposable? = null

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(layoutResId, container, false)

    @CallSuper
    override fun onStart() {
        super.onStart()

        statesDisposable = viewModel.states
                .distinctUntilChanged()
                .observeOn(mainThread())
                .subscribe({
                    // TODO disable sending events from fragment to view model when rendering state
                    renderState(it)
                    // TODO enable sending events
                }, {
                    throw IllegalStateException("Error notification must not reach the fragment - handle errors upstream in view model: $it")
                })
    }

    @CallSuper
    override fun onStop() {
        super.onStop()

        statesDisposable?.dispose()
        statesDisposable = null
    }

    protected abstract fun renderState(state: S)
}