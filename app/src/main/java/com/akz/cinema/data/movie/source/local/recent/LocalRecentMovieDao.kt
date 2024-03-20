package com.akz.cinema.data.movie.source.local.recent

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalRecentMovieDao {

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

    @Upsert
    suspend fun upsert(movie: LocalRecentMovie)

    @Upsert
    suspend fun upsertAll(movies: List<LocalRecentMovie>)

    @Query("DELETE FROM $RECENT_MOVIES_TABLE_NAME WHERE id is :id")
    suspend fun deleteOneById(id: Int)

    @Query("SELECT * FROM $RECENT_MOVIES_TABLE_NAME ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<LocalRecentMovie>>

    @Query("SELECT * FROM $RECENT_MOVIES_TABLE_NAME ORDER BY createdAt DESC")
    suspend fun loadAll(): List<LocalRecentMovie>

    @Query("DELETE FROM $RECENT_MOVIES_TABLE_NAME")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM $RECENT_MOVIES_TABLE_NAME")
    suspend fun getRecentMoviesCount(): Long

    @Query("SELECT COUNT(*) FROM $RECENT_MOVIES_TABLE_NAME")
    fun observeRecentMoviesCount(): Flow<Long>
}