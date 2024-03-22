package com.akz.cinema.ui.screen.detail

import com.akz.cinema.data.detail.MovieDetail

sealed interface DetailEvent {
    data object DeleteAll : DetailEvent
    class GetDetail(val movieId: Int) : DetailEvent
    class SaveDetail(val movieDetail: MovieDetail) : DetailEvent
    data object EnqueueLocalStorageWorkers : DetailEvent
    class DeleteDetail(val movieDetail: MovieDetail) : DetailEvent
}