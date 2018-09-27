package cz.jhutarek.marble.arch.resources.di

import cz.jhutarek.marble.arch.resources.device.AndroidStringsController
import cz.jhutarek.marble.arch.resources.domain.StringsController
import dagger.Binds
import dagger.Module

@Module
abstract class StringsModule {

    @Binds
    abstract fun provideStringsController(controller: AndroidStringsController): StringsController
}