package com.akz.cinema.data.di

import android.content.Context
import androidx.room.Room
import com.akz.cinema.data.database.MoviesDatabase
import com.akz.cinema.data.detail.source.local.LocalMovieDetailDao
import com.akz.cinema.data.movie.source.local.LocalMovieDao
import com.akz.cinema.data.movie.source.local.recent.LocalRecentMovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): MoviesDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MoviesDatabase::class.java,
            "movies.db"
        )
            .build()
    }

    @Provides
    fun provideLocalMovieDetailDao(moviesDatabase: MoviesDatabase): LocalMovieDetailDao {
        return moviesDatabase.getLocalMovieDetailDao()
    }

    @Provides
    fun provideLocalMovieDao(moviesDatabase: MoviesDatabase): LocalMovieDao {
        return moviesDatabase.getLocalMovieDao()
    }

    @Provides
    fun provideLocalRecentMovieDao(moviesDatabase: MoviesDatabase): LocalRecentMovieDao {
        return moviesDatabase.getLocalRecentMovieDao()
    }
}