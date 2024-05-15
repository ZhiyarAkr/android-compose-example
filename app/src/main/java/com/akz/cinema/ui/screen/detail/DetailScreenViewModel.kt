package com.akz.cinema.ui.screen.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.akz.cinema.data.detail.MovieDetail
import com.akz.cinema.data.detail.MovieDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val movieDetailRepository: MovieDetailRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _movieDetail: MutableStateFlow<MovieDetail?> = MutableStateFlow(null)
    val movieDetail = _movieDetail.asStateFlow()

    var saveToLocal by mutableStateOf(false)
        private set

    fun onEvent(detailEvent: DetailEvent) {
        when(detailEvent) {
            DetailEvent.DeleteAll -> deleteAllLocals()
            is DetailEvent.DeleteDetail -> {
                saveToLocal = false
            }
            is DetailEvent.GetDetail -> getMovieDetail(detailEvent.movieId)
            is DetailEvent.SaveDetail -> {
                saveToLocal = true
            }

            DetailEvent.EnqueueLocalStorageWorkers -> enqueueLocalStorageWorkers()
        }
    }

    init {
        val movieId = savedStateHandle.toRoute<DetailScreenRoute>().movieId
        getMovieDetail(movieId)
    }

    private fun getMovieDetail(movieId: Int) {
        viewModelScope.launch {
            try {
                val md = movieDetailRepository.getMovieDetail(movieId)
                _movieDetail.update {
                    md
                }
                saveToLocal = md?.isSavedInLocal ?: false
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

    private fun enqueueLocalStorageWorkers() {
        movieDetail.value?.let {
            if (!it.isSavedInLocal && saveToLocal) {
                movieDetailRepository.saveToLocal(it)
            } else if (it.isSavedInLocal && !saveToLocal) {
                movieDetailRepository.deleteOne(it)
            }
        }
    }
}