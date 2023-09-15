package com.akz.cinema.ui.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.flatMap
import com.akz.cinema.data.connectivity.Connectivity
import com.akz.cinema.data.movie.Movie
import com.akz.cinema.data.movie.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val connectivity: Connectivity
) : ViewModel() {
    private val _moviesOfWeek: MutableStateFlow<List<Movie>> = MutableStateFlow(emptyList())
    val moviesOfWeek = _moviesOfWeek.asStateFlow()

    var topPadding by mutableStateOf(0.dp)
        private set

    val nowPlayingMoviesStream = movieRepository.getNowPlayingMoviesStream()
        .cachedIn(viewModelScope)
        .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PagingData.empty()
    )


    private val localMovies: StateFlow<LocalResult> =
        movieRepository.observeLocalStoredMovies().map {
            LocalResult.Success(it)
        }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                LocalResult.Loading
            )

    private val isConnected = connectivity.isConnected
        .onEach {
            if (it) {
                if (_moviesOfWeek.value.isEmpty()) {
                    try {
                        val movies = movieRepository.fetchMoviesOfWeek()
                        _moviesOfWeek.update {
                            movies
                        }
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            false
        )


    init {
        refreshMoviesOfWeek()
    }

    fun onEvent(event: HomeEvent) {
        when(event) {
            HomeEvent.SaveAllMovies -> {
                viewModelScope.launch {
                    try {
                        movieRepository.deleteAllMovies()
                        movieRepository.saveMoviesToLocal(
                            _moviesOfWeek.value
                        )
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun updateTopPadding(padding: Dp) {
        topPadding = padding
    }



    private fun refreshMoviesOfWeek() {
        viewModelScope.launch {
            try {
                if (isConnected.value) {
                    val movies = movieRepository.fetchMoviesOfWeek()
                    _moviesOfWeek.update {
                        movies
                    }
                } else {
                    var completed = false
                    do {
                        localMovies.value.let { localResult ->
                            when (localResult) {
                                LocalResult.Error -> {
                                    delay(50)
                                }

                                LocalResult.Loading -> {
                                    delay(50)
                                }

                                is LocalResult.Success -> {
                                    _moviesOfWeek.update {
                                        localResult.data
                                    }
                                    completed = true
                                }
                            }
                        }

                    } while (!completed)

                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}

sealed interface LocalResult {
    data object Loading : LocalResult
    class Success(val data: List<Movie>) : LocalResult
    data object Error : LocalResult
}