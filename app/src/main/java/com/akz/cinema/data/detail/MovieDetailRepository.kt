package com.akz.cinema.data.detail

import com.akz.cinema.data.detail.source.local.LocalMovieDetailDao
import com.akz.cinema.data.detail.source.local.toMovieDetail
import com.akz.cinema.data.detail.source.local.toMovieDetails
import com.akz.cinema.data.detail.source.remote.MovieDetailApi
import com.akz.cinema.data.detail.source.remote.toMovieDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieDetailRepository @Inject constructor(
    private val movieDetailApi: MovieDetailApi,
    private val localMovieDetailDao: LocalMovieDetailDao
) {


    suspend fun saveToLocal(movieDetail: MovieDetail): MovieDetail {
        val local = movieDetail.toLocalMovieDetail()
        localMovieDetailDao.upsert(local)
        return movieDetail.copy(
            isSavedInLocal = true
        )
    }

    suspend fun clearLocal() {
        localMovieDetailDao.deleteAll()
    }

    suspend fun getMovieDetail(id: Int): MovieDetail? {
        return localMovieDetailDao.loadOneById(id)?.toMovieDetail() ?: kotlin.run {
            val res = movieDetailApi.getMovieDetails(id)
            if (res.isSuccessful) {
                res.body()?.toMovieDetail()
            } else {
                null
            }
        }
    }

    fun observeAll(): Flow<List<MovieDetail>> {
        return localMovieDetailDao.observeAll().map {
            it.toMovieDetails()
        }
    }

    suspend fun deleteOne(movieDetail: MovieDetail): MovieDetail {
        localMovieDetailDao.deleteOneById(movieDetail.id)
        return movieDetail.copy(
            isSavedInLocal = false
        )
    }
}