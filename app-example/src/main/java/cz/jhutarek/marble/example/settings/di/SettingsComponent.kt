package cz.jhutarek.marble.example.settings.di

import cz.jhutarek.marble.example.settings.system.SettingsFragment

interface SettingsComponent {
    fun inject(fragment: SettingsFragment)
}