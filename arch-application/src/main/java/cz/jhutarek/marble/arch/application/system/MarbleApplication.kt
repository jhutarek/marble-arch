package cz.jhutarek.marble.arch.application.system

import android.app.Application
import android.content.Context
import com.squareup.leakcanary.LeakCanary
import cz.jhutarek.marble.arch.log.infrastructure.Logger
import cz.jhutarek.marble.arch.log.infrastructure.logD
import cz.jhutarek.marble.arch.mvvm.system.Mvvm

// TODO test
abstract class MarbleApplication<C : Any> : Application() {

    protected object Injector {
        @Suppress("unchecked_cast")
        operator fun <C : Any> invoke(context: Context?) =
            ((context?.applicationContext) as? MarbleApplication<C>)?.component
                ?: throw IllegalStateException("Context must be an application context of type MarbleApplication with correct component type")
    }

    lateinit var component: C
        private set

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) return
        LeakCanary.install(this)

        Logger.initialize(isDebug, logTag)
        Mvvm.initialize()
        onInitialize()

        component = onCreateComponent()

        logD("Application created")
    }

    protected open fun onInitialize() {}

    protected abstract fun onCreateComponent(): C

    protected open val logTag = "*Mrbl:"

    protected abstract val isDebug: Boolean
}
