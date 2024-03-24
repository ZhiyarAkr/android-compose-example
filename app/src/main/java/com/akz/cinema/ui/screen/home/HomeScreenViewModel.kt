package com.akz.cinema.ui.screen.home

import android.graphics.drawable.Drawable
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
import com.akz.cinema.util.RemoteImageSize
import com.akz.cinema.util.getUriForRemoteImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
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

    private val selectedIndex = MutableStateFlow(0)

    var bottomPadding by mutableStateOf(0.dp)
        private set


    private val _moviesOfDay: MutableStateFlow<List<Movie>> = MutableStateFlow(emptyList())
    val moviesOfDay = _moviesOfDay.asStateFlow()

    private val _moviesOfWeek: MutableStateFlow<List<Movie>> = MutableStateFlow(emptyList())
    val moviesOfWeek = _moviesOfWeek.asStateFlow()

    val dominantSwatch = paletteManager.dominantSwatch.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    val nowPlayingMoviesStream = movieRepository.getNowPlayingMoviesStream()
        .cachedIn(viewModelScope)

    init {
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


    fun makePaletteFromMovieIndex(index: Int) {
        if (moviesOfDay.value.isNotEmpty()) {
            val uri = getUriForRemoteImage(
                moviesOfDay.value[index].backdropPath,
                RemoteImageSize.ImageSizeW780
            )
            if (uri != null) refreshPaletteFromUri(uri, 0.3f)
        }
    }

    private fun refreshPaletteFromUri(uri: String, lighting: Float) {
        viewModelScope.launch {
            paletteManager.makePaletteFromUri(uri, lighting)
        }
    }

    fun makePaletteFromDrawable(drawable: Drawable, lighting: Float) {
        viewModelScope.launch {
            paletteManager.makePaletteFromDrawable(drawable, lighting)
        }
    }

    fun updateSelectedIndex(index: Int) {
        selectedIndex.update {
            index
        }
    }

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

    fun updateBottomPadding(padding: Dp) {
        bottomPadding = padding
    }

}