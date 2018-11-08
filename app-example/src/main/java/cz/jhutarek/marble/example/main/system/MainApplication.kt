package cz.jhutarek.marble.example.main.system

import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import cz.jhutarek.marble.arch.application.system.MarbleApplication
import cz.jhutarek.marble.example.main.di.DaggerMainComponent
import cz.jhutarek.marble.example.main.di.MainComponent
import cz.jhutarek.marblearch.BuildConfig

class MainApplication : MarbleApplication<MainComponent>() {

    override val isDebug = BuildConfig.DEBUG

    object Injector {
        operator fun invoke(context: Context?) = MarbleApplication.Injector<MainComponent>(context)
    }

    override fun onCreateComponent() = DaggerMainComponent.builder()
        .applicationContext(applicationContext)
        .build()

    override fun onInitialize() {
        AndroidThreeTen.init(this)
    }
}