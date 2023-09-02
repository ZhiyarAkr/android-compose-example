package com.akz.cinema.data.di

import com.akz.cinema.data.detail.source.remote.MovieDetailApi
import com.akz.cinema.data.interceptor.TMDBHeaderInterceptor
import com.akz.cinema.data.movie.source.remote.MovieApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteMovieModule {

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(TMDBHeaderInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitInstance(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieApi(retrofit: Retrofit): MovieApi {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun provideMovieDetailApi(retrofit: Retrofit): MovieDetailApi {
        return retrofit.create()
    }
}