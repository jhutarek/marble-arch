package cz.jhutarek.marble.arch.mvvm.system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.jakewharton.rxrelay2.BehaviorRelay
import cz.jhutarek.marble.arch.mvvm.model.MarbleState
import cz.jhutarek.marble.arch.mvvm.presentation.MarbleViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.withLatestFrom
import javax.inject.Inject

// TODO test
abstract class MarbleFragment<M : MarbleViewModel<S>, S : MarbleState> : Fragment() {

    @Inject internal lateinit var __privateViewModel: M
    protected abstract val layoutResId: Int
    private var statesDisposable: Disposable? = null
    private val isUpdatingViewRelay = BehaviorRelay.createDefault(false)

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(layoutResId, container, false)

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBindViews()
    }

    @CallSuper
    override fun onStart() {
        super.onStart()

        statesDisposable = onBindStates(
                __privateViewModel.states
                        .distinctUntilChanged()
                        .observeOn(mainThread())
        )
    }

    @CallSuper
    override fun onStop() {
        super.onStop()

        statesDisposable?.dispose()
        statesDisposable = null
    }

    protected open fun onBindStates(states: Observable<S>): Disposable = Observable.never<Unit>().subscribe()

    protected open fun onBindViews() {}

    protected fun Observable<S>.subscribeForViews(onValue: (S) -> Unit): Disposable = subscribe {
        isUpdatingViewRelay.accept(true)
        onValue(it)
        isUpdatingViewRelay.accept(false)
    }

    protected fun <T> Observable<T>.subscribeForViewModel(onValue: M.(event: T) -> Unit): Disposable =
            this.withLatestFrom(isUpdatingViewRelay) { event, isUpdatingView -> Pair(event, isUpdatingView) }
                    .filter { !it.second }
                    .map { it.first }
                    .subscribe { onValue(__privateViewModel, it) }
}