package com.akz.cinema.data.detail

import com.akz.cinema.data.detail.source.local.LocalMovieDetail
import com.akz.cinema.data.detail.source.remote.Genre
import com.akz.cinema.data.detail.source.remote.ProductionCompany
import com.akz.cinema.data.detail.source.remote.ProductionCountry
import com.akz.cinema.data.detail.source.remote.SpokenLanguage

data class MovieDetail(
    val id: Int,
    val isAdult: Boolean,
    val backdropPath: String,
    val budget: Int,
    val genres: List<Genre>,
    val homePage: String,
    val overview: String,
    val productionCompanies: List<ProductionCompany>,
    val productionCountries: List<ProductionCountry>,
    val spokenLanguages: List<SpokenLanguage>,
    val status: String,
    val title: String,
    val releaseDate: String,
    val revenue: Int,
    val isSavedInLocal: Boolean = false
)

fun MovieDetail.toLocal() = LocalMovieDetail(
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
    createdAt = ""
)

fun List<MovieDetail>.toLocal(): List<LocalMovieDetail> = map(MovieDetail::toLocal)
