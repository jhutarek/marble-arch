package cz.jhutarek.marble.example.main.di

import android.content.Context
import cz.jhutarek.marble.arch.resources.di.StringsModule
import cz.jhutarek.marble.example.current.di.CurrentWeatherComponent
import cz.jhutarek.marble.example.current.di.CurrentWeatherModule
import cz.jhutarek.marble.example.overview.di.OverviewComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        CurrentWeatherModule::class,
        StringsModule::class
    ]
)
interface MainComponent : OverviewComponent, CurrentWeatherComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun applicationContext(context: Context): Builder

        fun build(): MainComponent
    }
}