package com.akz.cinema.data.movie

import com.akz.cinema.data.movie.source.local.LocalMovie
import com.akz.cinema.data.movie.source.local.recent.LocalRecentMovie


data class Movie(
    val isAdult: Boolean,
    val backdropPath: String?,
    val isImagePathAbsolute: Boolean = false,
    val id: Int,
    val language: String,
    val title: String,
    val overview: String,
    val releaseDate: String,
)

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
