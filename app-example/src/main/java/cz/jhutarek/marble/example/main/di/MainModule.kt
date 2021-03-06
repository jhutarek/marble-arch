package cz.jhutarek.marble.example.main.di

import cz.jhutarek.marble.arch.navigation.device.AndroidNavigationController
import cz.jhutarek.marble.arch.navigation.system.NavigationActivityDelegate
import cz.jhutarek.marble.example.main.system.MainActivity
import cz.jhutarek.marblearch.R
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module(includes = [MainModule.AbstractMainModule::class])
class MainModule {

    @Module
    interface AbstractMainModule {
        @ContributesAndroidInjector
        fun mainActivity(): MainActivity
    }

    @Provides
    @Singleton
    fun navigationActivityDelegate(navigationController: AndroidNavigationController) =
        NavigationActivityDelegate(navigationController, R.id.navigationHostFragment)
}