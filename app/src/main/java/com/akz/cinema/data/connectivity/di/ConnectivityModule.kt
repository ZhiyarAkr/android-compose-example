package com.akz.cinema.data.connectivity.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.getSystemService
import com.akz.cinema.data.connectivity.Connectivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectivityModule {

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return getSystemService(
            context,
            ConnectivityManager::class.java
        ) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideConnectivity(connectivityManager: ConnectivityManager) : Connectivity {
        return Connectivity(connectivityManager)
    }
}