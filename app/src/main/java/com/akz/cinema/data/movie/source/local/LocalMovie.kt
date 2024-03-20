package com.akz.cinema.data.movie.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.akz.cinema.data.movie.Movie

const val MOVIE_TABLE_NAME = "movies"

@Entity(tableName = MOVIE_TABLE_NAME)
data class LocalMovie(
    @PrimaryKey val id: Int,
    val isAdult: Boolean,
    val backdropPath: String?,
    val language: String,
    val title: String,
    val overview: String,
    val releaseDate: String,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP") val createdAt: String
)

fun LocalMovie.toMovie() = Movie(
    id = id,
    isAdult = isAdult,
    backdropPath = backdropPath,
    language = language,
    title = title,
    overview = overview,
    releaseDate = releaseDate
)

fun List<LocalMovie>.toMovies() = map(LocalMovie::toMovie)