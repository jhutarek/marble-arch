package cz.jhutarek.marble.arch.mvvm.system

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxrelay2.BehaviorRelay
import cz.jhutarek.marble.arch.log.infrastructure.logD
import cz.jhutarek.marble.arch.mvvm.model.State
import cz.jhutarek.marble.arch.mvvm.presentation.ViewModel
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.withLatestFrom
import javax.inject.Inject

// TODO test
// TODO extract common parts with MarbleFragment
abstract class MarbleActivity<M : ViewModel<S>, S : State> : AppCompatActivity() {

    @Inject protected lateinit var viewModel: M
    protected abstract val layoutResId: Int
    private var statesDisposable: Disposable? = null
    private var viewsDisposable: CompositeDisposable? = null
    private val isUpdatingViewRelay = BehaviorRelay.createDefault(false)

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        onBeforeCreate()

        super.onCreate(savedInstanceState)

        setContentView(layoutResId)

        AndroidInjection.inject(this)
        onInitializeViews()

        logD("Activity created")
    }

    @CallSuper
    override fun onStart() {
        super.onStart()

        logD("Bind views")
        viewsDisposable = CompositeDisposable(onBindViews())

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

        logD("Unbind views")
        viewsDisposable?.clear()
        viewsDisposable = null
    }

    protected open fun onBeforeCreate() {}

    protected open fun onInitializeViews() {}

    protected open fun onBindStates(states: Observable<S>): Disposable = Observable.never<Unit>().subscribe()

    protected open fun onBindViews(): List<Disposable> = listOf()

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