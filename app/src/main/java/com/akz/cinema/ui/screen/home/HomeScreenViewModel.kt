package com.akz.cinema.ui.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.akz.cinema.data.movie.Movie
import com.akz.cinema.data.movie.MovieRepository
import com.akz.cinema.util.PaletteManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val paletteManager: PaletteManager
) : ViewModel() {

    var topPadding by mutableStateOf(0.dp)
        private set

    var bottomPadding by mutableStateOf(0.dp)
        private set


    private val _moviesOfDay: MutableStateFlow<List<Movie>> = MutableStateFlow(emptyList())
    val moviesOfDay = _moviesOfDay.asStateFlow()

    private val _moviesOfWeek: MutableStateFlow<List<Movie>> = MutableStateFlow(emptyList())
    val moviesOfWeek = _moviesOfWeek.asStateFlow()

    val nowPlayingMoviesStream = movieRepository.getNowPlayingMoviesStream()
        .cachedIn(viewModelScope)

    init {
        refreshMovies()
    }

    private fun refreshMovies() {
        viewModelScope.launch {
            try {
                val dayMovies = movieRepository.fetchMoviesOfDay()
                val weekMovies = movieRepository.fetchMoviesOfWeek()
                _moviesOfDay.update {
                    dayMovies
                }
                _moviesOfWeek.update {
                    weekMovies
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.RefreshMovies -> {
                refreshMovies()
            }
        }
    }

    fun updateTopPadding(padding: Dp) {
        topPadding = padding
    }

    fun updateBottomPadding(padding: Dp) {
        bottomPadding = padding
    }

}