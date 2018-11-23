package cz.jhutarek.marble.arch.application.di

import cz.jhutarek.marble.arch.application.system.MarbleApplication

interface MarbleApplicationComponent {

    fun inject(marbleApplication: MarbleApplication)
}