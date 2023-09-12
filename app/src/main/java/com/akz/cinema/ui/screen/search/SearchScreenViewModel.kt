package com.akz.cinema.ui.screen.search

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.akz.cinema.data.movie.Movie
import com.akz.cinema.data.movie.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<Movie>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private val recentSearchResults = movieRepository.observeLocalStoredRecentMovies().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    var isSearchBarActive by mutableStateOf(false)
        private set

    var searchQuery by mutableStateOf("")
        private set

    var isHistoryBeingServed by mutableStateOf(true)
        private set

    @OptIn(FlowPreview::class)
    private val searchQueryDebounced = snapshotFlow { searchQuery }.debounce(1000).onEach {
        if (it.isNotBlank()) {
            try {
                val result = movieRepository.searchMovieByQuery(it)
                isHistoryBeingServed = false
                _searchResults.update {
                    result
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            isHistoryBeingServed = true
            _searchResults.update {
                recentSearchResults.value
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        ""
    )

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun updateIsSearchBarActive(isActive: Boolean) {
        isSearchBarActive = isActive
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.SearchMovieByQuery -> {
                viewModelScope.launch {
                    try {
                        val result = movieRepository.searchMovieByQuery(event.query)
                        event.keyboardController?.hide()
                        _searchResults.update {
                            result
                        }
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            }

            is SearchEvent.HideKeyboard -> {
                event.keyboardController?.hide()
            }

            is SearchEvent.SaveToRecent -> {
                viewModelScope.launch {
                    try {
                        val loader = ImageLoader(event.context)
                        val req = ImageRequest.Builder(event.context)
                            .data("https://image.tmdb.org/t/p/w500/${event.movie.backdropPath}")
                            .allowHardware(false)
                            .build()
                        val result = (loader.execute(req) as SuccessResult).drawable
                        val bitmap = result.toBitmap()
                        val outputStream = ByteArrayOutputStream(bitmap.byteCount)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        val inputStream = ByteArrayInputStream(outputStream.toByteArray())
                        inputStream.use { input ->
                            movieRepository.saveRecentMovieToLocal(
                                event.movie,
                                event.context,
                                input
                            )
                        }
                        movieRepository.checkRecentMoviesSizeAndFilterOld()
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}