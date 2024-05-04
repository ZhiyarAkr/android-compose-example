package com.akz.cinema.ui.screen.home.carousel

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.akz.cinema.data.movie.Movie
import com.akz.cinema.util.getUriForRemoteImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroCarouselCompose(
    modifier: Modifier = Modifier,
    movies: List<Movie>,
    onClick: (Int) -> Unit,
    onPositionChange: (Int) -> Unit
) {
    HorizontalMultiBrowseCarousel(
        modifier = modifier
            .width(412.dp)
            .height(221.dp),
        state = rememberCarouselState {
            movies.size
        },
        itemSpacing = 8.dp,
        preferredItemWidth = 186.dp
    ) {
        Spacer(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Red)
        )
//        Card(
//            modifier = Modifier.height(205.dp)
//        ) {
//            AsyncImage(
//                modifier = Modifier
//                    .clickable { onClick(movies[it].id) },
//                model = getUriForRemoteImage(movies[it].backdropPath),
//                contentDescription = null,
//                contentScale = ContentScale.Crop
//            )
//        }
    }
}