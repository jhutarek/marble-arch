package cz.jhutarek.marble.example.main.di

import android.content.Context
import cz.jhutarek.marble.example.current.di.CurrentWeatherComponent
import cz.jhutarek.marble.example.overview.di.OverviewComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface MainComponent : OverviewComponent, CurrentWeatherComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun applicationContext(context: Context): Builder

        fun build(): MainComponent
    }
}