package com.akz.cinema.data.detail.worker

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coil.ImageLoader
import coil.request.ImageRequest
import com.akz.cinema.data.detail.source.local.LocalMovieDetailDao
import com.akz.cinema.data.detail.source.remote.MovieDetailApi
import com.akz.cinema.data.detail.source.remote.toExternal
import com.akz.cinema.data.detail.toLocal
import com.akz.cinema.util.RemoteImageSize
import com.akz.cinema.util.getUriForRemoteImage
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File
import java.time.Instant

class SaveMovieDetailsToLocalWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    @Assisted private val localMovieDetailDao: LocalMovieDetailDao,
    @Assisted private val movieDetailApi: MovieDetailApi
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val id = params.inputData.getInt(MOVIE_DETAIL_ID, 0)
        if (id == 0) return Result.failure()
        try {
            val res = movieDetailApi.getMovieDetails(id).body()?.toExternal() ?: return Result.retry()
            val imageRequest = ImageRequest.Builder(context)
                .data(getUriForRemoteImage(res.backdropPath, RemoteImageSize.ImageSizeW780))
                .allowConversionToBitmap(true)
                .allowHardware(false)
                .build()
            val image = ImageLoader(context).execute(imageRequest).drawable ?: return Result.retry()
            val bitmap = image.toBitmap()
            val folder = File(context.filesDir, MOVIE_DETAIL_DIRECTORY_NAME)
            if (!folder.exists()) {
                folder.mkdir()
            }
            val fileName = getRandomName()
            val file = File(folder, fileName)
            file.outputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, file.outputStream())
            localMovieDetailDao.upsert(res.toLocal().copy(
                backdropPath = fileName
            ))
            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.retry()
        }
    }

    private fun getRandomName(): String {
        val instant = Instant.now().toEpochMilli()
        return "${instant}_detailImage.jpg"
    }

    companion object {
        const val MOVIE_DETAIL_ID = "MovieDetailId"
        const val MOVIE_DETAIL_DIRECTORY_NAME = "detail-images"
    }
}
