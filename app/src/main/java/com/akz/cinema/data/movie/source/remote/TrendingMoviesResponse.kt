package com.akz.cinema.data.movie.source.remote

data class TrendingMoviesResponse(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)