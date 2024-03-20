package com.akz.cinema.util.di

import com.akz.cinema.util.PaletteManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {

    @Provides
    fun providePaletteManager() = PaletteManager()
}