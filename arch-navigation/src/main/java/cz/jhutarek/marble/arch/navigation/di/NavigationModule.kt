package cz.jhutarek.marble.arch.navigation.di

import cz.jhutarek.marble.arch.navigation.device.AndroidNavigationController
import cz.jhutarek.marble.arch.navigation.domain.NavigationController
import dagger.Binds
import dagger.Module

@Module
interface NavigationModule {

    @Binds
    fun navigationController(controller: AndroidNavigationController): NavigationController
}