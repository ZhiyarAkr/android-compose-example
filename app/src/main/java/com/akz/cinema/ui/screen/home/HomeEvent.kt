package com.akz.cinema.ui.screen.home

sealed interface HomeEvent {
    data object SaveAllMovies : HomeEvent
}