package cz.jhutarek.marble.arch.mvvm.system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.jakewharton.rxrelay2.BehaviorRelay
import cz.jhutarek.marble.arch.log.infrastructure.logD
import cz.jhutarek.marble.arch.mvvm.model.State
import cz.jhutarek.marble.arch.mvvm.presentation.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.withLatestFrom
import javax.inject.Inject

// TODO test
// TODO extract common parts with MarbleActivity
abstract class MarbleFragment<M : ViewModel<S>, S : State> : Fragment() {

    @Inject protected lateinit var viewModel: M
    protected abstract val layoutResId: Int
    private var statesDisposable: Disposable? = null
    private val isUpdatingViewRelay = BehaviorRelay.createDefault(false)

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(layoutResId, container, false)

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onInitializeViews()
        onBindViews()
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onInjection()

        logD("Fragment created")
    }

    @CallSuper
    override fun onStart() {
        super.onStart()

        logD("Bind states")
        statesDisposable = onBindStates(
            viewModel.states
                .distinctUntilChanged()
                .observeOn(mainThread())
        )
    }

    @CallSuper
    override fun onStop() {
        super.onStop()

        logD("Unbind states")
        statesDisposable?.dispose()
        statesDisposable = null
    }

    protected abstract fun onInjection()

    protected open fun onInitializeViews() {}

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
            .subscribe { onValue(viewModel, it) }
}