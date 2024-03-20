package com.akz.cinema.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akz.cinema.data.detail.MovieDetail
import com.akz.cinema.data.detail.MovieDetailRepository
import com.akz.cinema.data.movie.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val movieDetailRepository: MovieDetailRepository,
    private val movieRepository: MovieRepository
) : ViewModel() {
    private val _movieDetail: MutableStateFlow<MovieDetail?> = MutableStateFlow(null)
    val movieDetail = _movieDetail.asStateFlow()

    fun onEvent(detailEvent: DetailEvent) {
        when(detailEvent) {
            DetailEvent.DeleteAll -> deleteAllLocals()
            is DetailEvent.DeleteDetail -> deleteDetailFromLocal(detailEvent.movieDetail)
            is DetailEvent.GetDetail -> getMovieDetail(detailEvent.movieId)
            is DetailEvent.SaveDetail -> saveDetailToLocal(detailEvent.movieDetail)
        }
    }

    private fun getMovieDetail(movieId: Int) {
        viewModelScope.launch {
            try {
                val md = movieDetailRepository.getMovieDetail(movieId)
                _movieDetail.update {
                    md
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    private fun saveDetailToLocal(movieDetail: MovieDetail) {
        viewModelScope.launch {
            try {
                val saved = movieDetailRepository.saveToLocal(movieDetail)
                _movieDetail.update {
                    saved
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    private fun deleteAllLocals() {
        viewModelScope.launch {
            try {
                movieDetailRepository.clearLocal()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    private fun deleteDetailFromLocal(movieDetail: MovieDetail) {
        viewModelScope.launch {
            try {
                val deleted = movieDetailRepository.deleteOne(movieDetail)
                _movieDetail.update {
                    deleted
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

}