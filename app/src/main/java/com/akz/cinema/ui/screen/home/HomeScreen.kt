package com.akz.cinema.ui.screen.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.currentStateAsState
import androidx.paging.compose.collectAsLazyPagingItems
import com.akz.cinema.LocalPaddings
import com.akz.cinema.ui.components.LazyMoviesHorizontalScroll
import com.akz.cinema.ui.screen.home.carousel.HeroCarousel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onDetailPressed: (Int) -> Unit
) {
    val moviesOfWeek by viewModel.moviesOfWeek.collectAsStateWithLifecycle()
    val moviesOfDay by viewModel.moviesOfDay.collectAsStateWithLifecycle()
    val paletteOutput by viewModel.dominantSwatch.collectAsStateWithLifecycle()
    val moviesStream = viewModel.nowPlayingMoviesStream.collectAsLazyPagingItems()
    val paddingValues = LocalPaddings.current
    val scrollState = rememberScrollState()
    val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateAsState()

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

    val secondBgColorOpacity by remember {
        derivedStateOf {
            1 - scrollState.value.toFloat() / scrollState.maxValue
        }
    }
    val color = MaterialTheme.colorScheme.background

    val paletteColorAnimated by animateColorAsState(
        targetValue = paletteOutput ?: color,
        label = "palette_color_animation",
        animationSpec = tween(
            durationMillis = 500
        )
    )


    val mainScreenModifier = if (isSystemInDarkTheme()) {
        Modifier
            .drawBehind {
                drawRect(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to paletteColorAnimated.copy(alpha = secondBgColorOpacity),
                            0.8f to color
                        )
                    )
                )
            }
    } else {
        Modifier
    }

    LaunchedEffect(pullToRefreshState.isRefreshing) {
        viewModel.onEvent(HomeEvent.RefreshMovies)
        moviesStream.refresh()
        delay(1000)
        pullToRefreshState.endRefresh()
    }

    Box(
        modifier = mainScreenModifier
            .fillMaxSize()
            .statusBarsPadding()
            .nestedScroll(pullToRefreshState.nestedScrollConnection),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HeroCarousel(
                modifier = Modifier.padding(top = 32.dp),
                movies = moviesOfDay,
                onClick = onDetailPressed,
                onPositionChange = viewModel::makePaletteFromMovieIndex
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Top movies of the week",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
                LazyMoviesHorizontalScroll(
                    movies = moviesOfWeek,
                    onClick = onDetailPressed
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
        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullToRefreshState
        )
    }
}