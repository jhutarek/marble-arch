package cz.jhutarek.marble.example.main.di

import android.content.Context
import cz.jhutarek.marble.example.counter.di.CounterComponent
import cz.jhutarek.marble.example.counter.di.CounterModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CounterModule::class])
interface MainComponent : CounterComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun applicationContext(context: Context): Builder

        fun build(): MainComponent
    }
}