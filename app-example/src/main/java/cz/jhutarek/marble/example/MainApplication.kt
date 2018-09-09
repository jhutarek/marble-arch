package cz.jhutarek.marble.example

import android.app.Application
import android.content.Context
import com.squareup.leakcanary.LeakCanary
import cz.jhutarek.marble.arch.mvvm.system.MarbleMvvm
import dagger.Component
import javax.inject.Singleton

class MainApplication : Application() {

    companion object {
        fun getInjector(context: Context?) = (context?.applicationContext as? MainApplication)
                ?.component
                ?: throw IllegalStateException("Cannot obtain injector when context is null")
    }

    private lateinit var component: MainComponent

    @Singleton
    @Component
    interface MainComponent {
        fun inject(fragment: CounterFragment)
    }

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) return
        LeakCanary.install(this)

        MarbleMvvm.initialize()

        component = DaggerMainApplication_MainComponent.create()
    }
}