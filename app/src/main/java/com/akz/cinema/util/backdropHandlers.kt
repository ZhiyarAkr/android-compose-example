package com.akz.cinema.util

import android.content.Context
import com.akz.cinema.data.detail.worker.SaveMovieDetailsToLocalWorker
import java.io.File

enum class RemoteImageSize {
    ImageSizeW500,
    ImageSizeW780,
    ImageSizeW1280,
    ImageSizeOriginal
}
fun getUriForRemoteImage(
    backdrop: String?,
    imageSize: RemoteImageSize = RemoteImageSize.ImageSizeW500
): String? {
    if (backdrop == null) return null
    return when(imageSize) {
        RemoteImageSize.ImageSizeW500 -> "https://image.tmdb.org/t/p/w500/$backdrop"
        RemoteImageSize.ImageSizeW780 -> "https://image.tmdb.org/t/p/w780/$backdrop"
        RemoteImageSize.ImageSizeW1280 -> "https://image.tmdb.org/t/p/w1280/$backdrop"
        RemoteImageSize.ImageSizeOriginal -> "https://image.tmdb.org/t/p/original/$backdrop"
    }
}

fun getUriForLocalDetailImage(backdrop: String, context: Context): String {
    val folder = File(context.filesDir, SaveMovieDetailsToLocalWorker.MOVIE_DETAIL_DIRECTORY_NAME)
    val file = File(folder, backdrop)
    return file.absolutePath
}