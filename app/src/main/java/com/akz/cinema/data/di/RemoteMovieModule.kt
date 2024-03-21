package com.akz.cinema.data.di

import com.akz.cinema.data.detail.source.remote.MovieDetailApi
import com.akz.cinema.data.interceptor.TMDBHeaderInterceptor
import com.akz.cinema.data.movie.source.remote.MovieApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteMovieModule {

    @Provides
    @Singleton
    fun provideOkhttpClient(): Call.Factory {
        return OkHttpClient.Builder()
            .addInterceptor(TMDBHeaderInterceptor())
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitInstance(callFactory: dagger.Lazy<Call.Factory>): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .callFactory {
                callFactory.get().newCall(it)
            }
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