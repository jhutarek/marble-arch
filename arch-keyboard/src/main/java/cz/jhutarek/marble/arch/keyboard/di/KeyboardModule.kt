package cz.jhutarek.marble.arch.keyboard.di

import cz.jhutarek.marble.arch.keyboard.device.AndroidKeyboardController
import cz.jhutarek.marble.arch.keyboard.domain.KeyboardController
import dagger.Binds
import dagger.Module

@Module
interface KeyboardModule {

    @Binds
    fun keyboardController(androidKeyboardController: AndroidKeyboardController): KeyboardController
}