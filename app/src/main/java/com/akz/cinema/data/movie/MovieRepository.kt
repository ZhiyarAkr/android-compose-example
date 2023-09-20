package com.akz.cinema.data.movie

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.akz.cinema.data.movie.source.local.LocalMovie
import com.akz.cinema.data.movie.source.local.LocalMovieDao
import com.akz.cinema.data.movie.source.local.recent.LocalRecentMovieDao
import com.akz.cinema.data.movie.source.local.recent.toMovies
import com.akz.cinema.data.movie.source.local.toMovies
import com.akz.cinema.data.movie.source.remote.MovieApi
import com.akz.cinema.data.movie.source.remote.NowPlayingPagingSource
import com.akz.cinema.data.movie.source.remote.toMovies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.File
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieApi: MovieApi,
    private val localMovieDao: LocalMovieDao,
    private val localRecentMovieDao: LocalRecentMovieDao
) {
    suspend fun fetchMoviesOfWeek(): List<Movie> {
        var movies: List<Movie> = listOf()
        val response = movieApi.fetchTrendingMoviesWeek()
        if (response.isSuccessful) {
            response.body()?.let {
                val results = it.results
                movies = withContext(Dispatchers.Default) {
                    results.toMovies()
                }
            }
        }
        return movies
    }

    fun getNowPlayingMoviesStream(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { NowPlayingPagingSource(movieApi) }
        ).flow
    }

    fun getLocalMoviesStream(): Flow<PagingData<LocalMovie>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                localMovieDao.loadMoviesStreamFromLocal()
            }
        ).flow
    }

    suspend fun saveMoviesToLocal(movies: List<Movie>) {
        val localMovies = withContext(Dispatchers.Default) {
            movies.toLocalMovies()
        }
        localMovieDao.upsertAll(localMovies)
    }

    fun observeLocalStoredMovies(): Flow<List<Movie>> {
        return localMovieDao.observeAll().map {
            withContext(Dispatchers.Default) {
                it.toMovies()
            }
        }
    }

    fun observeLocalStoredRecentMovies(): Flow<List<Movie>> {
        return localRecentMovieDao.observeAll().map {
            withContext(Dispatchers.Default) {
                it.toMovies()
            }
        }
    }

    suspend fun deleteAllRecentMovies() {
        localRecentMovieDao.deleteAll()
    }

    suspend fun deleteOneRecentMovieById(id: Int) {
        localRecentMovieDao.deleteOneById(id)
    }

    suspend fun saveRecentMovieToLocal(movie: Movie, context: Context, inputStream: ByteArrayInputStream) {
        val target = if (context.filesDir.usableSpace >= inputStream.available()) {
            context.filesDir
        } else {
            context.getExternalFilesDir(null)
        }
        var filePath: String? = null

        movie.backdropPath?.let {
            val file = File(target, it)
            file.outputStream().use { output ->
                inputStream.copyTo(output)
            }
            filePath = file.absolutePath
        }

        localRecentMovieDao.insert(
            id = movie.id,
            isAdult = movie.isAdult,
            title = movie.title,
            overview = movie.overview,
            releaseDate = movie.releaseDate,
            language = movie.language,
            backdropPath = filePath
        )
    }

    suspend fun checkRecentMoviesSizeAndFilterOld() {
        var recentMovies = localRecentMovieDao.loadAll()
        if (recentMovies.size > 10) {
            do {
                val movie = recentMovies.last()
                movie.backdropPath?.let {
                    val file = File(it)
                    file.delete()
                }
                recentMovies = recentMovies.dropLast(1)
            } while (recentMovies.size > 10)
            localRecentMovieDao.deleteAll()
            localRecentMovieDao.upsertAll(recentMovies)
        }
    }

    suspend fun deleteAllMovies() {
        localMovieDao.deleteAll()
    }

    suspend fun searchMovieByQuery(query: String, page: Int = 1): List<Movie> {
        var movies: List<Movie> = emptyList()
        val response = movieApi.searchMoviesByQuery(query, page)
        if (response.isSuccessful) {
            response.body()?.let {
                val results = it.results
                movies = withContext(Dispatchers.Default) {
                    results.toMovies()
                }
            }
        }
        return movies
    }

    suspend fun fetchMoviesOfDay(): List<Movie> {
        var movies: List<Movie> = listOf()
        val response = movieApi.fetchTrendingMoviesDay()
        if (response.isSuccessful) {
            response.body()?.let {
                val results = it.results
                movies = withContext(Dispatchers.Default) {
                    results.toMovies()
                }
            }
        }
        return movies
    }
}