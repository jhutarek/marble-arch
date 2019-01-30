package cz.jhutarek.marble.arch.application.system

import android.app.Activity
import android.app.Application
import android.app.Service
import androidx.fragment.app.Fragment
import com.squareup.leakcanary.LeakCanary
import cz.jhutarek.marble.arch.application.di.MarbleApplicationComponent
import cz.jhutarek.marble.arch.log.infrastructure.Logger
import cz.jhutarek.marble.arch.log.infrastructure.logD
import cz.jhutarek.marble.arch.mvvm.system.Mvvm
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

// TODO test
abstract class MarbleApplication : Application(), HasActivityInjector, HasSupportFragmentInjector, HasServiceInjector {

    @Inject internal lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>
    @Inject internal lateinit var dispatchingFragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject internal lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    internal lateinit var component: MarbleApplicationComponent

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) return
        LeakCanary.install(this)

        Logger.initialize(isDebug, logTag)
        Mvvm.initialize()
        onInitialize()

        component = onCreateComponent()
            .also { it.inject(this) }

        logD("Application created")
    }

    final override fun activityInjector() = dispatchingActivityInjector

    final override fun supportFragmentInjector() = dispatchingFragmentInjector

    final override fun serviceInjector() = dispatchingServiceInjector

    protected open fun onInitialize() {}

    protected abstract fun onCreateComponent(): MarbleApplicationComponent

    protected open val logTag = "*Mrbl:"

    protected abstract val isDebug: Boolean
}
