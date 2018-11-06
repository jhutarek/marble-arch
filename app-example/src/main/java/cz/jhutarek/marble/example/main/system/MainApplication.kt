package cz.jhutarek.marble.example.main.system

import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import cz.jhutarek.marble.arch.application.system.MarbleApplication
import cz.jhutarek.marble.arch.mvvm.system.Mvvm
import cz.jhutarek.marble.example.main.di.DaggerMainComponent
import cz.jhutarek.marble.example.main.di.MainComponent
import cz.jhutarek.marblearch.BuildConfig
import timber.log.Timber

class MainApplication : MarbleApplication<MainComponent>() {

    companion object {
        fun getInjector(context: Context?) = MarbleApplication.getInjector<MainComponent>(context)
    }

    private class TagPrefixDebugTree(private val tagPrefix: String) : Timber.DebugTree() {
        override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) =
            super.log(priority, tagPrefix + tag, message, throwable)
    }

    override fun onCreateComponent() = DaggerMainComponent.builder()
        .applicationContext(applicationContext)
        .build()

    override fun onInitialize() {
        if (BuildConfig.DEBUG) {
            Timber.plant(TagPrefixDebugTree("*MA: "))
        }

        AndroidThreeTen.init(this)
        Mvvm.initialize()
    }
}