package com.akz.cinema.data.movie.source.remote

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.akz.cinema.data.movie.Movie
import com.akz.cinema.data.movie.source.remote.nowplaying.toMovies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class NowPlayingPagingSource@Inject constructor(
    private val movieApi: MovieApi
): PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        var ret = listOf<Movie>()
        val position = params.key ?: 1
        try {
            val response = movieApi.fetchNowPlayingMoviesByPage(position)
            val remoteMovies = withContext(Dispatchers.Default) {
                response.body()?.results?.toMovies()
            }
            remoteMovies?.let {
                ret = it
            }
            val nextKey = if (remoteMovies?.isEmpty() != false) {
                null
            } else {
                position + (params.loadSize / 20)
            }
            return LoadResult.Page(
                data = ret,
                prevKey = if (position == 1) null else position - 1,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        }
    }

}