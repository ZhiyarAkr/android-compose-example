package com.akz.cinema.data.movie.source.remote.nowplaying

import com.akz.cinema.data.movie.Movie

data class Result(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)

fun Result.toMovie() = Movie(
    isAdult = adult,
    backdropPath = backdrop_path,
    id = id,
    language = original_language,
    title = title,
    overview = overview,
    releaseDate = release_date
)

fun List<Result>.toMovies() = map(Result::toMovie)