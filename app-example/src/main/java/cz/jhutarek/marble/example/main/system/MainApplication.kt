package cz.jhutarek.marble.example.main.system

import android.app.Application
import android.content.Context
import com.squareup.leakcanary.LeakCanary
import cz.jhutarek.marble.arch.mvvm.system.Mvvm
import cz.jhutarek.marble.example.main.di.DaggerMainComponent
import cz.jhutarek.marble.example.main.di.MainComponent
import cz.jhutarek.marblearch.BuildConfig
import timber.log.Timber

class MainApplication : Application() {

    private class TagPrefixDebugTree(private val tagPrefix: String) : Timber.DebugTree() {
        override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) =
                super.log(priority, tagPrefix + tag, message, throwable)
    }

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

        if (BuildConfig.DEBUG) {
            Timber.plant(TagPrefixDebugTree("*MA: "))
        }

        Mvvm.initialize()

        component = DaggerMainComponent.builder()
                .applicationContext(applicationContext)
                .build()
    }
}