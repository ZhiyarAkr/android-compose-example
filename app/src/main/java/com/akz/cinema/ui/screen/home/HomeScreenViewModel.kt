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
import androidx.paging.map
import com.akz.cinema.data.connectivity.Connectivity
import com.akz.cinema.data.movie.MovieRepository
import com.akz.cinema.data.movie.source.local.toMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val connectivity: Connectivity
) : ViewModel() {

    var topPadding by mutableStateOf(0.dp)
        private set

    private val localMovieStream = movieRepository.getLocalMoviesStream()
        .map {
            withContext(Dispatchers.Default) {
                it.map { movie->
                    movie.toMovie()
                }
            }
        }
        .cachedIn(viewModelScope)

    private val remoteMovieStream =
        movieRepository.getNowPlayingMoviesStream().cachedIn(viewModelScope)

    private val isConnected = connectivity.isConnected
    @OptIn(ExperimentalCoroutinesApi::class)
    val nowPlayingMoviesStream = isConnected.flatMapLatest { connected ->
        if (connected) {
            remoteMovieStream
        } else {
            localMovieStream
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PagingData.empty()
    )



    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SaveAllMovies -> {
                viewModelScope.launch {
                    try {
                        movieRepository.deleteAllMovies()
                        movieRepository.saveMoviesToLocal(
                            event.movies
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

}