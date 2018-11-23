package cz.jhutarek.marble.example.main.di

import android.content.Context
import cz.jhutarek.marble.arch.application.di.MarbleApplicationComponent
import cz.jhutarek.marble.arch.navigation.di.NavigationModule
import cz.jhutarek.marble.arch.resources.di.StringsModule
import cz.jhutarek.marble.example.current.di.CurrentWeatherModule
import cz.jhutarek.marble.example.overview.di.OverviewModule
import cz.jhutarek.marble.example.settings.di.SettingsModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        MainModule::class,
        CurrentWeatherModule::class,
        OverviewModule::class,
        SettingsModule::class,
        StringsModule::class,
        NavigationModule::class
    ]
)
interface MainComponent : MarbleApplicationComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun applicationContext(context: Context): Builder

        fun build(): MainComponent
    }
}