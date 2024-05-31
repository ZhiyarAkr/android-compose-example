package com.akz.cinema.ui.screen.home.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselItemScope
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.akz.cinema.data.movie.Movie
import com.akz.cinema.ui.components.Skeleton
import com.akz.cinema.util.RemoteImageSize
import com.akz.cinema.util.getUriForRemoteImage
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroCarouselCompose(
    modifier: Modifier = Modifier,
    state: CarouselState,
    preferredItemWidth: Dp,
    titleStyle: TextStyle = MaterialTheme.typography.titleSmall,
    movies: List<Movie>,
    onClick: (Int) -> Unit
) {
    HeroHorizontalCarousel(
        modifier = modifier,
        state = state,
        preferredItemWidth = preferredItemWidth
    ) {
        HeroCarouselItem(
            modifier = Modifier.fillMaxSize(),
            movies = movies,
            onClick = onClick,
            titleStyle = titleStyle,
            index = it
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HeroHorizontalCarousel(
    modifier: Modifier = Modifier,
    state: CarouselState,
    preferredItemWidth: Dp,
    content: @Composable CarouselItemScope.(Int) -> Unit
) {
    HorizontalMultiBrowseCarousel(
        modifier = modifier
            .width(412.dp)
            .height(221.dp),
        state = state,
        itemSpacing = 8.dp,
        preferredItemWidth = preferredItemWidth,
        contentPadding = PaddingValues(horizontal = 16.dp),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CarouselItemScope.HeroCarouselItem(
    modifier: Modifier = Modifier,
    movies: List<Movie>,
    onClick: (Int) -> Unit,
    titleStyle: TextStyle = MaterialTheme.typography.titleSmall,
    index: Int
) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .height(205.dp)
            .maskClip(shape = RoundedCornerShape(32.dp))

    ) {
        if (movies.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
                val sharedElementString = "image_${movies[index].id}"
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onClick(movies[index].id) },
                    model = ImageRequest.Builder(context)
                        .data(
                            getUriForRemoteImage(
                                movies[index].backdropPath,
                                RemoteImageSize.ImageSizeW780
                            )
                        )
                        .memoryCacheKey(sharedElementString)
                        .placeholderMemoryCacheKey(sharedElementString)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = movies[index].title,
                    style = titleStyle,
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                x = this@HeroCarouselItem.carouselItemInfo.maskRect.left.roundToInt(),
                                y = 0
                            )
                        }
                        .graphicsLayer {
                            alpha = lerp(
                                1f,
                                0f,
                                this@HeroCarouselItem.carouselItemInfo.maskRect.left / 80f
                            )
                        }
                        .padding(start = 16.dp, bottom = 16.dp)
                        .background(
                            Color.Black.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(100)
                        )
                        .padding(8.dp),
                    color = Color.White
                )
            }
        } else {
            Skeleton(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}