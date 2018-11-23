package cz.jhutarek.marble.arch.resources.di

import cz.jhutarek.marble.arch.resources.device.AndroidStringsController
import cz.jhutarek.marble.arch.resources.domain.StringsController
import dagger.Binds
import dagger.Module

@Module
interface StringsModule {

    @Binds
    fun stringsController(controller: AndroidStringsController): StringsController
}