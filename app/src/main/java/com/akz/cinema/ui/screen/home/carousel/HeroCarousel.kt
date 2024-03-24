package com.akz.cinema.ui.screen.home.carousel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.RecyclerView
import com.akz.cinema.data.movie.Movie
import com.akz.cinema.ui.screen.home.carousel.uitls.OnSnapPositionChangeListener
import com.akz.cinema.ui.screen.home.carousel.uitls.OnSnapPositionChangeListenerImpl
import com.akz.cinema.ui.screen.home.carousel.uitls.SnapOnScrollListener
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.HeroCarouselStrategy

@Composable
fun HeroCarousel(
    modifier: Modifier = Modifier,
    movies: List<Movie> = emptyList(),
    onClick: (Int) -> Unit,
    onPositionChange: (Int) -> Unit
) {
    val color = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)
    var selected by rememberSaveable {
        mutableIntStateOf(0)
    }
    val adp = remember {
        HeroAdapter(color.toArgb(), onClick).apply {
            setMovies(movies)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),

        ) {
        Text(
            text = "Top movies of the day",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
        )
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(196.dp),
            factory = { context ->
                RecyclerView(context).apply {
                    clipChildren = false
                    clipToPadding = false
                    adapter = adp
                    layoutManager = CarouselLayoutManager(HeroCarouselStrategy())
                    val csh = CarouselSnapHelper()
                    csh.attachToRecyclerView(this)
                    val onSnapPositionChangeListenerImpl = OnSnapPositionChangeListenerImpl {
                        selected = it
                        onPositionChange(it)
                    }
                    addOnScrollListener(
                        SnapOnScrollListener(
                            snapHelper = csh,
                            onSnapPositionChangeListener = onSnapPositionChangeListenerImpl
                        )
                    )
                }
            },
            update = {
                adp.setMovies(movies)
                it.layoutManager?.scrollToPosition(selected)
                onPositionChange(selected)
            }
        )
    }
}