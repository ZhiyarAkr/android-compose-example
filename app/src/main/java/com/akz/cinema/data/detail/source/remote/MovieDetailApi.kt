package com.akz.cinema.data.detail.source.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface MovieDetailApi {
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int): Response<RemoteMovieDetailResponse>
}