package com.akz.cinema

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import androidx.work.Configuration
import com.akz.cinema.data.detail.worker.MovieDetailsWorkerFactory
import javax.inject.Inject

@HiltAndroidApp
class Application: Application(), Configuration.Provider {

    @Inject lateinit var factory: MovieDetailsWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(factory)
            .build()
}