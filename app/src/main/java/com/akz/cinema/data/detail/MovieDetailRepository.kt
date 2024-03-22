package com.akz.cinema.data.detail

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.akz.cinema.data.detail.source.local.LocalMovieDetailDao
import com.akz.cinema.data.detail.source.local.toExternal
import com.akz.cinema.data.detail.source.remote.MovieDetailApi
import com.akz.cinema.data.detail.source.remote.toExternal
import com.akz.cinema.data.detail.worker.DeleteMovieDetailsFromLocalWorker
import com.akz.cinema.data.detail.worker.SaveMovieDetailsToLocalWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Duration
import javax.inject.Inject

class MovieDetailRepository @Inject constructor(
    private val movieDetailApi: MovieDetailApi,
    private val localMovieDetailDao: LocalMovieDetailDao,
    @ApplicationContext private val context: Context
) {


    fun saveToLocal(movieDetail: MovieDetail) {
        val wm = WorkManager.getInstance(context)
        val workRequest = OneTimeWorkRequestBuilder<SaveMovieDetailsToLocalWorker>()
            .setInputData(workDataOf(
                SaveMovieDetailsToLocalWorker.MOVIE_DETAIL_ID to movieDetail.id
            ))
            .setConstraints(Constraints(requiredNetworkType = NetworkType.CONNECTED))
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, Duration.ofMinutes(1))
            .build()
        val workId = "save_details_to_local_${movieDetail.id}"
        wm.enqueueUniqueWork(workId, ExistingWorkPolicy.KEEP, workRequest)
    }


    suspend fun clearLocal() {
        localMovieDetailDao.deleteAll()
    }

    suspend fun getMovieDetail(id: Int): MovieDetail? {
        return localMovieDetailDao.loadOneById(id)?.toExternal() ?: kotlin.run {
            val res = movieDetailApi.getMovieDetails(id)
            if (res.isSuccessful) {
                res.body()?.toExternal()
            } else {
                null
            }
        }
    }

    fun observeAll(): Flow<List<MovieDetail>> {
        return localMovieDetailDao.observeAll().map {
            it.toExternal()
        }
    }

    fun deleteOne(movieDetail: MovieDetail) {
        val wm = WorkManager.getInstance(context)
        val workRequest = OneTimeWorkRequestBuilder<DeleteMovieDetailsFromLocalWorker>()
            .setInputData(workDataOf(
                DeleteMovieDetailsFromLocalWorker.MOVIE_DETAIL_ID to movieDetail.id
            ))
            .build()
        val workId = "delete_details_from_local_${movieDetail.id}"
        wm.enqueueUniqueWork(workId, ExistingWorkPolicy.KEEP, workRequest)
    }
}