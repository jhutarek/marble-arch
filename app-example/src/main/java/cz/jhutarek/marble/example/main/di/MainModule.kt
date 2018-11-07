package cz.jhutarek.marble.example.main.di

import cz.jhutarek.marble.arch.navigation.device.AndroidNavigationController
import cz.jhutarek.marble.arch.navigation.system.NavigationActivityDelegate
import cz.jhutarek.marblearch.R
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule {

    @Provides
    @Singleton
    fun provideNavigationMainActivityDelegate(navigationController: AndroidNavigationController) =
        NavigationActivityDelegate(navigationController, R.id.navigationHostFragment)
}