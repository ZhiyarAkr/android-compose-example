package com.akz.cinema.ui.screen.home

import com.akz.cinema.data.movie.Movie

sealed interface HomeEvent {
    data class SaveAllMovies(val movies: List<Movie>) : HomeEvent
}