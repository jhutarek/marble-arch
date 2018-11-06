package cz.jhutarek.marble.arch.navigation.di

import cz.jhutarek.marble.arch.navigation.device.AndroidNavigationController
import cz.jhutarek.marble.arch.navigation.domain.NavigationController
import dagger.Binds
import dagger.Module

@Module
abstract class NavigationModule {

    @Binds
    abstract fun provideNavigationController(controller: AndroidNavigationController): NavigationController
}