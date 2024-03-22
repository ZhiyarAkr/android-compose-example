package com.akz.cinema.ui.screen.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akz.cinema.data.detail.MovieDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SavedScreenViewModel @Inject constructor(
    movieDetailRepository: MovieDetailRepository
) : ViewModel() {
    val localMovieDetails = movieDetailRepository.observeAll().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
}