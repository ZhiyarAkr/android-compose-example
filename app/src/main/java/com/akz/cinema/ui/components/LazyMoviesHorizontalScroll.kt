package com.akz.cinema.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.akz.cinema.data.movie.Movie
import com.akz.cinema.ui.theme.CinemaTheme
import com.akz.cinema.util.getUriForRemoteImage

private val RowHeight = 200.dp
private val ItemWidth = 120.dp
private val CornerRadius = 16.dp

@Composable
fun LazyMoviesHorizontalScroll(
    modifier: Modifier = Modifier,
    movies: List<Movie>,
    onClick: (Int) -> Unit
) {
    MoviesLazyRow(
        modifier = modifier
    ) {
        if (movies.isEmpty()) {
            skeletonList()
        } else {
            items(
                items = movies,
                key = { it.id }
            ) { movie ->
                MovieItem(movie = movie, onClick = onClick)
            }
        }
    }
}

@Composable
fun LazyMoviesHorizontalScroll(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<Movie>,
    onClick: (Int) -> Unit
) {
    MoviesLazyRow(
        modifier = modifier
    ) {
        if (lazyPagingItems.itemCount == 0) {
            skeletonList()
        } else {
            items(
                count = lazyPagingItems.itemCount,
//                key = lazyPagingItems.itemKey { it.id },
                contentType = lazyPagingItems.itemContentType { it::class.java }
            ) {
                val item = lazyPagingItems[it]
                item?.let { movie ->
                    MovieItem(movie = movie, onClick = onClick)
                }
            }
        }
    }
}

@Composable
private fun MoviesLazyRow(
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(RowHeight)
            .padding(vertical = 16.dp),
        contentPadding = PaddingValues(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        content()
    }
}

@Composable
private fun MovieItem(
    modifier: Modifier = Modifier,
    movie: Movie,
    onClick: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(ItemWidth),
    ) {
        AsyncImage(
            model = getUriForRemoteImage(movie.backdropPath),
            contentDescription = "Movie image",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.66f)
                .clip(RoundedCornerShape(CornerRadius))
                .clickable { onClick(movie.id) },
            contentScale = ContentScale.Crop
        )
        Text(
            text = movie.title,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(vertical = 8.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}


private fun LazyListScope.skeletonList(
    modifier: Modifier = Modifier
) {
    val list = (0..19).toList()
    items(
        items = list
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .width(ItemWidth)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.66f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(CornerRadius))
            ) {
                Skeleton()
            }
            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(start = 8.dp)
                    .height(16.dp)
                    .width(80.dp)
                    .clip(RoundedCornerShape(8.dp))

            ) {
                Skeleton()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LazyMoviesHorizontalScrollPreview() {
    val movies = listOf(
        Movie(
            isAdult = false,
            backdropPath = null,
            id = 0,
            language = "en",
            title = "Movie1",
            overview = "",
            releaseDate = "1/1/2024"
        ),
        Movie(
            isAdult = false,
            backdropPath = null,
            id = 1,
            language = "en",
            title = "Movie2",
            overview = "",
            releaseDate = "1/1/2024"
        )
    )
    CinemaTheme(
        dynamicColor = false
    ) {
        LazyMoviesHorizontalScroll(movies = movies, onClick = {})
    }
}
