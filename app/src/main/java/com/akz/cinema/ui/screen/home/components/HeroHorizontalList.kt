package com.akz.cinema.ui.screen.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.akz.cinema.data.movie.Movie

@Composable
fun HeroHorizontalList(
    movies: List<Movie>,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    state: PagerState = rememberPagerState { if (movies.isNotEmpty()) movies.size else 10 },
) {
    HeroHorizontalPager(
        pageCount = if (movies.isNotEmpty()) movies.size else 10,
        modifier = modifier,
        state = state
    ) { page ->
        if (movies.isNotEmpty()) {
            HeroCard(movie = movies[page], onClick = onClick)
        } else {
            Card(
                modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(16.dp)
            ) {

            }
        }
    }
//    LazyRow(
//        modifier = modifier
//            .padding(top = 16.dp)
//            .fillMaxWidth(),
//        contentPadding = PaddingValues(horizontal = 16.dp),
//    ) {
//        if (movies.isNotEmpty()) {
//            items(
//                items = movies,
//                key = { it.id }
//            ) {
//                HeroCard(movie = it)
//            }
//        } else {
//            items(count = 10, key = { it })
//            {
//                Skeleton(
//                    modifier
//                        .width(300.dp)
//                        .height(400.dp)
//                        .padding(16.dp)
//                        .clip(RoundedCornerShape(16.dp))
//                )
//            }
//        }
//    }
}

@Composable
private fun HeroHorizontalPager(
    pageCount: Int,
    modifier: Modifier = Modifier,
    state: PagerState = rememberPagerState { pageCount },
    content: @Composable (page: Int) -> Unit
) {
    HorizontalPager(
        state = state,
        modifier = modifier.fillMaxWidth()
    ) { page ->
        content(page)
    }
}