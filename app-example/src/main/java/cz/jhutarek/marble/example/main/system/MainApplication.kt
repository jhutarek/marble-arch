package cz.jhutarek.marble.example.main.system

import com.jakewharton.threetenabp.AndroidThreeTen
import cz.jhutarek.marble.arch.application.system.MarbleApplication
import cz.jhutarek.marble.example.main.di.DaggerMainComponent
import cz.jhutarek.marblearch.BuildConfig

class MainApplication : MarbleApplication() {

    override val isDebug = BuildConfig.DEBUG

    override fun onCreateComponent() = DaggerMainComponent.builder()
        .applicationContext(applicationContext)
        .build()

    override fun onInitialize() {
        AndroidThreeTen.init(this)
    }
}