package cz.jhutarek.marble.arch.application.system

import android.app.Application
import android.content.Context
import com.squareup.leakcanary.LeakCanary

// TODO test
abstract class MarbleApplication<C : Any> : Application() {

    companion object {
        @JvmStatic
        @Suppress("unchecked_cast")
        protected fun <C : Any> getInjector(context: Context?) =
            ((context?.applicationContext) as? MarbleApplication<C>)?.component
                ?: throw IllegalStateException("Context must be an application context of type MarbleApplication with correct component type")
    }

    lateinit var component: C
        private set

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) return
        LeakCanary.install(this)

        onInitialize()

        component = onCreateComponent()
    }

    protected open fun onInitialize() {}

    protected abstract fun onCreateComponent(): C
}
