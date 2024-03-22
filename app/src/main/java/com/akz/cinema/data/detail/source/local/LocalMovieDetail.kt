package com.akz.cinema.data.detail.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.akz.cinema.data.detail.MovieDetail

@Entity(tableName = "detail_table")
data class LocalMovieDetail(
    @PrimaryKey val id: Int,
    val isAdult: Boolean,
    val backdropPath: String,
    val budget: Int,
    val homePage: String,
    val overview: String,
    val status: String,
    val title: String,
    val releaseDate: String,
    val revenue: Int,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP") val createdAt: String
)

fun LocalMovieDetail.toExternal() = MovieDetail(
    id = id,
    isAdult = isAdult,
    backdropPath = backdropPath,
    budget = budget,
    homePage = homePage,
    overview = overview,
    status = status,
    title = title,
    releaseDate = releaseDate,
    revenue = revenue,
    genres = emptyList(),
    productionCountries = emptyList(),
    productionCompanies = emptyList(),
    spokenLanguages = emptyList(),
    isSavedInLocal = true
)

fun List<LocalMovieDetail>.toExternal(): List<MovieDetail> = map(LocalMovieDetail::toExternal)
