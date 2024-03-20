package com.akz.cinema.data.movie.source.remote

import com.akz.cinema.data.movie.source.remote.nowplaying.NowPlayingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("trending/movie/week?language=en-US")
    suspend fun fetchTrendingMoviesWeek(): Response<TrendingMoviesResponse>

    @GET("trending/movie/day?language=en-US")
    suspend fun fetchTrendingMoviesDay(): Response<TrendingMoviesResponse>

    @GET("search/movie?")
    suspend fun searchMoviesByQuery(@Query("query") query: String, @Query("page") page: Int = 1): Response<TrendingMoviesResponse>

    @GET("movie/now_playing?language=en-US&")
    suspend fun fetchNowPlayingMoviesByPage(@Query("page") page: Int = 1): Response<NowPlayingResponse>
}