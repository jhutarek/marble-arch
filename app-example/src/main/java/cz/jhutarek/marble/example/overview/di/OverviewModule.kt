package cz.jhutarek.marble.example.overview.di

import cz.jhutarek.marble.example.overview.system.OverviewFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class OverviewModule {

    @ContributesAndroidInjector
    abstract fun overviewFragment(): OverviewFragment
}