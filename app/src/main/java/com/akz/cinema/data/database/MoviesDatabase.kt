package com.akz.cinema.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.akz.cinema.data.detail.source.local.LocalMovieDetail
import com.akz.cinema.data.detail.source.local.LocalMovieDetailDao
import com.akz.cinema.data.movie.source.local.LocalMovie
import com.akz.cinema.data.movie.source.local.LocalMovieDao
import com.akz.cinema.data.movie.source.local.recent.LocalRecentMovie
import com.akz.cinema.data.movie.source.local.recent.LocalRecentMovieDao

@Database(
    entities = [LocalMovieDetail::class, LocalMovie::class, LocalRecentMovie::class],
    version = 1,
    exportSchema = true
)
abstract class MoviesDatabase: RoomDatabase() {
    abstract fun getLocalMovieDetailDao(): LocalMovieDetailDao
    abstract fun getLocalMovieDao(): LocalMovieDao
    abstract fun getLocalRecentMovieDao(): LocalRecentMovieDao
}