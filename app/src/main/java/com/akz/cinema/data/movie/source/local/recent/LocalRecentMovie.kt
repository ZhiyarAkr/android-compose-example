package com.akz.cinema.data.movie.source.local.recent

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.akz.cinema.data.movie.Movie

const val RECENT_MOVIES_TABLE_NAME = "recent_movies"

@Entity(tableName = RECENT_MOVIES_TABLE_NAME)
data class LocalRecentMovie(
    @PrimaryKey val id: Int,
    val isAdult: Boolean,
    val backdropPath: String?,
    val language: String,
    val title: String,
    val overview: String,
    val releaseDate: String,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP") val createdAt: String
)

fun LocalRecentMovie.toExternal() = Movie(
    id = id,
    isAdult = isAdult,
    backdropPath = backdropPath,
    isImagePathAbsolute = true,
    language = language,
    title = title,
    overview = overview,
    releaseDate = releaseDate
)

fun List<LocalRecentMovie>.toExternal() = map(LocalRecentMovie::toExternal)