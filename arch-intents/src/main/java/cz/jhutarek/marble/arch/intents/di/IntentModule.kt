package cz.jhutarek.marble.arch.intents.di

import cz.jhutarek.marble.arch.intents.device.AndroidIntentController
import cz.jhutarek.marble.arch.intents.domain.IntentController
import dagger.Binds
import dagger.Module

@Module
interface IntentModule {

    @Binds
    fun intentController(controller: AndroidIntentController): IntentController
}