package cz.jhutarek.marble.arch.application.system

import android.app.Application
import android.content.Context
import com.squareup.leakcanary.LeakCanary
import cz.jhutarek.marble.arch.log.infrastructure.Logger
import cz.jhutarek.marble.arch.mvvm.system.Mvvm

// TODO test
abstract class MarbleApplication<C : Any> : Application() {

    protected object Injector {
        @Suppress("unchecked_cast")
        fun <C : Any> get(context: Context?) =
            ((context?.applicationContext) as? MarbleApplication<C>)?.component
                ?: throw IllegalStateException("Context must be an application context of type MarbleApplication with correct component type")
    }

    lateinit var component: C
        private set

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) return
        LeakCanary.install(this)

        Logger.initialize(logTag)
        Mvvm.initialize()
        onInitialize()

        component = onCreateComponent()
    }

    protected open fun onInitialize() {}

    protected abstract fun onCreateComponent(): C

    protected open val logTag = "*Mrbl:"
}
