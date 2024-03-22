package com.akz.cinema.data.detail.source.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalMovieDetailDao {

    @Query("SELECT * FROM detail_table")
    fun observeAll(): Flow<List<LocalMovieDetail>>

    @Upsert
    suspend fun upsert(localMovieDetail: LocalMovieDetail): Long

    @Query("SELECT * FROM detail_table WHERE id is :id LIMIT 1")
    suspend fun loadOneById(id: Int): LocalMovieDetail?

    @Upsert
    suspend fun upsertAll(localMovieDetails: List<LocalMovieDetail>)

    @Query("DELETE FROM detail_table")
    suspend fun deleteAll()

    @Query("DELETE FROM detail_table WHERE id is :id")
    suspend fun deleteOneById(id: Int)
}