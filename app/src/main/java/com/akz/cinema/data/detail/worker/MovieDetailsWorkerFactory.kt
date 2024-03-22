package com.akz.cinema.data.detail.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.akz.cinema.data.detail.source.local.LocalMovieDetailDao
import com.akz.cinema.data.detail.source.remote.MovieDetailApi
import javax.inject.Inject

class MovieDetailsWorkerFactory @Inject constructor(
    private val localMovieDetailDao: LocalMovieDetailDao,
    private val movieDetailApi: MovieDetailApi
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? = when(workerClassName) {
        SaveMovieDetailsToLocalWorker::class.java.name -> SaveMovieDetailsToLocalWorker(
            appContext,
            workerParameters,
            localMovieDetailDao,
            movieDetailApi
        )
        DeleteMovieDetailsFromLocalWorker::class.java.name -> DeleteMovieDetailsFromLocalWorker(
            appContext,
            workerParameters,
            localMovieDetailDao
        )
        else -> null
    }
}