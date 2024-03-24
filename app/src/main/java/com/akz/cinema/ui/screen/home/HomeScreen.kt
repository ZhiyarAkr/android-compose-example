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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.currentStateAsState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.ImageLoader
import coil.request.ImageRequest
import com.akz.cinema.LocalPaddings
import com.akz.cinema.LocalTopAppBarState
import com.akz.cinema.ui.components.LazyMoviesHorizontalScroll
import com.akz.cinema.ui.screen.home.carousel.HeroCarousel


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
    val context = LocalContext.current
    val topAppBarState = LocalTopAppBarState.current
    val paddingValues = LocalPaddings.current
    val scrollState = rememberScrollState()
    val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateAsState()
    var topBarHeightOffset by rememberSaveable {
        mutableFloatStateOf(0f)
    }
    var topBarContentOffset by rememberSaveable {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(paddingValues, lifecycleState) {
        if (lifecycleState == Lifecycle.State.RESUMED) {
            viewModel.updateBottomPadding(paddingValues.calculateBottomPadding())
        }
    }

    LaunchedEffect(Unit) {
        topAppBarState.contentOffset = topBarContentOffset
        topAppBarState.heightOffset = topBarHeightOffset
    }

    val padding = LocalPaddings.current.calculateTopPadding()
    LaunchedEffect(padding) {
        viewModel.updateTopPadding(padding)
    }


    val onClick = remember {
        { id: Int ->
            topBarContentOffset = topAppBarState.contentOffset
            topBarHeightOffset = topAppBarState.heightOffset
            onDetailPressed(id)
        }
    }


    val secondBgColorOpacity by remember {
        derivedStateOf {
            1 - scrollState.value.toFloat() / scrollState.maxValue
        }
    }

    val paletteColorAnimated by animateColorAsState(
        targetValue = paletteOutput ?: Color.Transparent,
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
                            0.8f to Color.Transparent
                        )
                    )
                )
            }
    } else {
        Modifier
    }

    LaunchedEffect(moviesOfDay) {
        if (moviesOfDay.isNotEmpty()) {
            moviesOfDay.firstOrNull()?.let { m ->
                val req = ImageRequest.Builder(context)
                    .allowHardware(false)
                    .data("https://image.tmdb.org/t/p/w500/${m.backdropPath}")
                    .build()
                val image = ImageLoader(context).execute(req)
                image.drawable?.let { d ->
                    viewModel.makePaletteFromDrawable(d, 0.3f)
                }
            }
        }
    }

    val heroScale = remember {
        {
            derivedStateOf {
                1f - 0.2f * (scrollState.value.toFloat() / scrollState.maxValue)
            }
        }
    }

    Box(
        modifier = mainScreenModifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
//            HomePageHeroCard(
//                movie = moviesOfDay.firstOrNull(),
//                scale = heroScale,
//                onClick = {
//                    onDetailPressed(it)
//                }
//            )
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
                    onClick = onClick
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
                    onClick = onClick
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