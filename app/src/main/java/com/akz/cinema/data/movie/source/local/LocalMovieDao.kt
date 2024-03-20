package com.akz.cinema.data.movie.source.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.akz.cinema.data.movie.source.local.recent.RECENT_MOVIES_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalMovieDao {
    @Upsert
    suspend fun upsertAll(movies: List<LocalMovie>)

    @Upsert
    suspend fun upsert(movie: LocalMovie)

    @Query("SELECT * FROM $MOVIE_TABLE_NAME")
    fun observeAll(): Flow<List<LocalMovie>>

    @Query("SELECT * FROM $MOVIE_TABLE_NAME")
    fun loadMoviesStreamFromLocal(): PagingSource<Int, LocalMovie>

    @Query("INSERT OR IGNORE INTO $RECENT_MOVIES_TABLE_NAME" +
            "(id, isAdult, backdropPath, language, title, overview, releaseDate) " +
            "VALUES(:id, :isAdult, :backdropPath, :language, :title, :overview, :releaseDate)"
    )
    suspend fun insert(
        id: Int,
        isAdult: Boolean,
        backdropPath: String?,
        language: String,
        title: String,
        overview: String,
        releaseDate: String
    )

    @Query("SELECT * FROM $MOVIE_TABLE_NAME WHERE id IS :id")
    suspend fun getOneById(id: Int): LocalMovie

    @Query("SELECT COUNT(*) FROM $MOVIE_TABLE_NAME WHERE id IS :id")
    suspend fun checkIfExistsById(id: Int): Boolean

    @Query("DELETE FROM $MOVIE_TABLE_NAME WHERE id is :id")
    suspend fun deleteOneById(id: Int)

    @Query("DELETE FROM $MOVIE_TABLE_NAME")
    suspend fun deleteAll()
}