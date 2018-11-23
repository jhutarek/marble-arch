package cz.jhutarek.marble.example.settings.di

import cz.jhutarek.marble.example.settings.system.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SettingsModule {

    @ContributesAndroidInjector
    abstract fun settingsFragment(): SettingsFragment
}