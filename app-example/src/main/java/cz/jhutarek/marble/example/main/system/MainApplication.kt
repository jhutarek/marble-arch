package cz.jhutarek.marble.example.main.system

import android.app.Application
import android.content.Context
import com.squareup.leakcanary.LeakCanary
import cz.jhutarek.marble.arch.mvvm.system.Mvvm
import cz.jhutarek.marble.example.main.di.DaggerMainComponent
import cz.jhutarek.marble.example.main.di.MainComponent

class MainApplication : Application() {

    companion object {
        fun getInjector(context: Context?) = (context?.applicationContext as? MainApplication)
                ?.component
                ?: throw IllegalStateException("Cannot obtain injector when context is null")
    }

    private lateinit var component: MainComponent

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) return
        LeakCanary.install(this)

        Mvvm.initialize()

        component = DaggerMainComponent.create()
    }
}