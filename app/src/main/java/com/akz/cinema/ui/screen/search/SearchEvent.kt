package com.akz.cinema.ui.screen.search

import android.content.Context
import androidx.compose.ui.platform.SoftwareKeyboardController
import com.akz.cinema.data.movie.Movie

sealed interface SearchEvent {
    data class SearchMovieByQuery(
        val query: String,
        val keyboardController: SoftwareKeyboardController?
    ) : SearchEvent

    data class HideKeyboard(val keyboardController: SoftwareKeyboardController?): SearchEvent
    data class SaveToRecent(
        val movie: Movie,
        val context: Context
    ): SearchEvent
}