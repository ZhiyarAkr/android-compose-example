package com.akz.cinema.ui.screen.home

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.currentStateAsState
import androidx.paging.compose.collectAsLazyPagingItems
import com.akz.cinema.LocalPaddings
import com.akz.cinema.ui.components.LazyMoviesHorizontalScroll
import com.akz.cinema.ui.screen.home.carousel.HeroCarouselCompose
import com.akz.cinema.ui.screen.home.components.HeroHorizontalList
import com.akz.cinema.ui.theme.mainColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onDetailPressed: (Int) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val moviesOfWeek by viewModel.moviesOfWeek.collectAsStateWithLifecycle()
    val moviesOfDay by viewModel.moviesOfDay.collectAsStateWithLifecycle()
    val moviesStream = viewModel.nowPlayingMoviesStream.collectAsLazyPagingItems()
    val paddingValues = LocalPaddings.current
    val scrollState = rememberScrollState()
    val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateAsState()
    val density = LocalDensity.current

    LaunchedEffect(paddingValues, lifecycleState) {
        if (lifecycleState == Lifecycle.State.RESUMED) {
            viewModel.updateBottomPadding(paddingValues.calculateBottomPadding())
        }
    }

    val padding = LocalPaddings.current.calculateTopPadding()
    LaunchedEffect(padding) {
        viewModel.updateTopPadding(padding)
    }

    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { if (moviesOfDay.isNotEmpty()) moviesOfDay.size else 10 }
    val isFirstPage by remember { derivedStateOf { pagerState.currentPage == 0 } }
    val animatedSlope by animateFloatAsState(
        label = "animatedSlope",
        targetValue = if (isFirstPage) 0f else with(density) { 12.dp.toPx() })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            state = pullToRefreshState,
            onRefresh = {
                isRefreshing = true
                scope.launch {
                    viewModel.onEvent(HomeEvent.RefreshMovies)
                    moviesStream.refresh()
                    delay(1000)
                    isRefreshing = false
                }
            }) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = scrollState),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                        .heroBackground(animatedSlope = animatedSlope, MaterialTheme.colorScheme.background)
                        .padding(vertical = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Top movies of the day",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .padding(start = 16.dp),
                        fontWeight = FontWeight.Bold
                    )
                    HeroHorizontalList(
                        movies = moviesOfDay,
                        onClick = onDetailPressed,
                        state = pagerState
                    )
//                    HeroCarouselCompose(
//                        movies = moviesOfDay,
//                        preferredItemWidth = 400.dp,
//                        state = rememberCarouselState { if (moviesOfDay.isNotEmpty()) moviesOfDay.size else 10 },
//                        onClick = onDetailPressed
//                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Top movies of the week",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    HeroCarouselCompose(
                        state = rememberCarouselState {
                            if (moviesOfWeek.isNotEmpty()) moviesOfWeek.size else 10
                        },
                        preferredItemWidth = 200.dp,
                        titleStyle = MaterialTheme.typography.bodySmall,
                        movies = moviesOfWeek,
                        onClick = onDetailPressed,
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Now playing movies",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    LazyMoviesHorizontalScroll(
                        lazyPagingItems = moviesStream,
                        onClick = onDetailPressed
                    )
                }
                Spacer(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .fillMaxWidth()
                        .height(viewModel.bottomPadding)
                )
            }
        }
    }
}

private fun Modifier.heroBackground(animatedSlope: Float, backgroundColor: Color) = drawWithCache {
    val path = Path()
    val upperPath = Path()
    val lowerPath = Path()
    val xFromLeft = 32.dp.toPx()
    val slope = 12.dp.toPx()
    val hSlope = size.height - slope
    val xFromLeft2 = xFromLeft + slope
    val xFromRight = size.width - xFromLeft
    val xFromRight2 = xFromRight - slope

    path.moveTo(0f, 0f)
    path.lineTo(xFromLeft, 0f)
    upperPath.moveTo(xFromLeft, 0f)
    upperPath.relativeLineTo(slope, slope)
    upperPath.lineTo(xFromRight2, slope)
    upperPath.relativeLineTo(slope, -animatedSlope)
    upperPath.lineTo(size.width, slope - animatedSlope)
    upperPath.lineTo(size.width, 0f)
    upperPath.close()
    path.relativeLineTo(slope, slope)
    path.lineTo(xFromRight2, slope)
    path.relativeLineTo(slope, -animatedSlope)
    path.lineTo(size.width, slope-animatedSlope)
    path.moveTo(size.width, size.height - (slope - animatedSlope))
    lowerPath.moveTo(size.width, size.height - (slope - animatedSlope))
    path.lineTo(xFromRight, size.height - (slope - animatedSlope))
    lowerPath.lineTo(xFromRight, size.height - (slope - animatedSlope))
    path.relativeLineTo(-slope, -animatedSlope)
    lowerPath.relativeLineTo(-slope, -animatedSlope)
    path.lineTo(xFromLeft2, hSlope)
    lowerPath.lineTo(xFromLeft2, hSlope)
    path.relativeLineTo(-slope, slope)
    lowerPath.relativeLineTo(-slope, slope)
    lowerPath.lineTo(size.width, size.height)
    path.lineTo(0f, size.height)
    onDrawBehind {
        drawPath(upperPath, color = backgroundColor)
        drawPath(lowerPath, color = backgroundColor)
        drawPath(path, color = mainColor, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))
    }
}