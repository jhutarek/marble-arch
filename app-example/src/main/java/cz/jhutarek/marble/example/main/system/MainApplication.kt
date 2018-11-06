package cz.jhutarek.marble.example.main.system

import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import cz.jhutarek.marble.arch.application.system.MarbleApplication
import cz.jhutarek.marble.example.main.di.DaggerMainComponent
import cz.jhutarek.marble.example.main.di.MainComponent

class MainApplication : MarbleApplication<MainComponent>() {

    object Injector {
        fun get(context: Context?) = MarbleApplication.Injector.get<MainComponent>(context)
    }

    override fun onCreateComponent() = DaggerMainComponent.builder()
        .applicationContext(applicationContext)
        .build()

    override fun onInitialize() {
        AndroidThreeTen.init(this)
    }
}