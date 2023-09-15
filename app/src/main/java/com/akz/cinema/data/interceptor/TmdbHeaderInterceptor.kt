package com.akz.cinema.data.interceptor

import com.akz.cinema.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class TMDBHeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer $ACCESS_TOKEN")
            .build()
        return chain.proceed(request)
    }

    companion object {
        const val ACCESS_TOKEN = BuildConfig.TMDB_AUTH_TOKEN
    }
}