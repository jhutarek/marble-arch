package cz.jhutarek.marble.example.overview.di

import cz.jhutarek.marble.example.overview.system.OverviewFragment

interface OverviewComponent {
    fun inject(fragment: OverviewFragment)
}