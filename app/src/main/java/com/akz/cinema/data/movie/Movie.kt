package com.akz.cinema.data.movie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import com.akz.cinema.data.movie.source.local.LocalMovie
import com.akz.cinema.data.movie.source.local.recent.LocalRecentMovie
import com.akz.cinema.util.RemoteImageSize
import com.akz.cinema.util.getUriForLocalDetailImage
import com.akz.cinema.util.getUriForRemoteImage


data class Movie(
    val isAdult: Boolean,
    val backdropPath: String?,
    val isImagePathAbsolute: Boolean = false,
    val id: Int,
    val language: String,
    val title: String,
    val overview: String,
    val releaseDate: String,
) {
    @ReadOnlyComposable
    @Composable
    fun getImageUrl(size: RemoteImageSize = RemoteImageSize.ImageSizeW500) = if (isImagePathAbsolute) {
        backdropPath?.let {
            getUriForLocalDetailImage(it, LocalContext.current)
        } ?: ""
    } else {
        backdropPath?.let {
            getUriForRemoteImage(backdropPath, size)
        }
    }
}

fun Movie.toLocalMovie() = LocalMovie(
    id = id,
    backdropPath = backdropPath,
    isAdult = isAdult,
    language = language,
    title = title,
    overview = overview,
    releaseDate = releaseDate,
    createdAt = ""
)

fun Movie.toLocalRecentMovie() = LocalRecentMovie(
    id = id,
    backdropPath = backdropPath,
    isAdult = isAdult,
    language = language,
    title = title,
    overview = overview,
    releaseDate = releaseDate,
    createdAt = ""
)

fun List<Movie>.toLocalMovies() = map(Movie::toLocalMovie)
fun List<Movie>.toLocalRecentMovies() = map(Movie::toLocalRecentMovie)
