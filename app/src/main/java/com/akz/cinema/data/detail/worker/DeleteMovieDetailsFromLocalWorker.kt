package com.akz.cinema.data.detail.worker

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coil.ImageLoader
import coil.request.ImageRequest
import com.akz.cinema.data.detail.source.local.LocalMovieDetailDao
import com.akz.cinema.data.detail.source.local.toExternal
import com.akz.cinema.data.detail.source.remote.MovieDetailApi
import com.akz.cinema.data.detail.source.remote.toExternal
import com.akz.cinema.data.detail.toLocal
import com.akz.cinema.util.RemoteImageSize
import com.akz.cinema.util.getUriForRemoteImage
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File
import java.time.Instant

class DeleteMovieDetailsFromLocalWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    @Assisted private val localMovieDetailDao: LocalMovieDetailDao
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val id = params.inputData.getInt(MOVIE_DETAIL_ID, 0)
        if (id == 0) return Result.failure()
        val res = localMovieDetailDao.loadOneById(id) ?: return Result.failure()
        return try {
            val folder = File(context.filesDir, "detail-images")
            val file = File(folder, res.backdropPath)
            if (file.exists()) {
                file.delete()
            }
            localMovieDetailDao.deleteOneById(res.id)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        const val MOVIE_DETAIL_ID = "MovieDetailId"
    }
}