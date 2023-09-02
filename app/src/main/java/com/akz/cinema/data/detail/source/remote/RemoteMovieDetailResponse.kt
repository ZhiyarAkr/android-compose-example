package com.akz.cinema.data.detail.source.remote

import com.akz.cinema.data.detail.MovieDetail

data class RemoteMovieDetailResponse(
    val adult: Boolean,
    val backdrop_path: String,
    val belongs_to_collection: Any,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val imdb_id: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val release_date: String,
    val revenue: Int,
    val runtime: Int,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)

fun RemoteMovieDetailResponse.toMovieDetail() = MovieDetail(
    id = id,
    isAdult = adult,
    backdropPath = backdrop_path,
    budget = budget,
    genres = genres,
    homePage = homepage,
    overview = overview,
    productionCompanies = production_companies,
    productionCountries = production_countries,
    spokenLanguages = spoken_languages,
    status = status,
    title = title,
    releaseDate = release_date,
    revenue = revenue
)