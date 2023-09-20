package com.akz.cinema.data.movie.source.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalMovieDao {
    @Upsert
    suspend fun upsertAll(movies: List<LocalMovie>)

    @Query("SELECT * FROM $MOVIE_TABLE_NAME")
    fun observeAll(): Flow<List<LocalMovie>>

    @Query("SELECT * FROM $MOVIE_TABLE_NAME")
    fun loadMoviesStreamFromLocal(): PagingSource<Int, LocalMovie>

    @Query("DELETE FROM $MOVIE_TABLE_NAME")
    suspend fun deleteAll()
}