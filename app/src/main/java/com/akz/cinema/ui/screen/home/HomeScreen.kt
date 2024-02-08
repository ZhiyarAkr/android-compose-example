package com.akz.cinema.ui.screen.home

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.akz.cinema.LocalPaddings
import com.akz.cinema.LocalTopAppBarState
import com.akz.cinema.ui.components.SaveMoviesInteraction
import com.akz.cinema.ui.components.SearchInteraction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    interactionSource: MutableInteractionSource,
    onDetailPressed: (Int) -> Unit,
    onSearchIconPressed: () -> Unit,
) {
//    val movies by viewModel.moviesOfWeek.collectAsStateWithLifecycle()
    val moviesStream = viewModel.nowPlayingMoviesStream.collectAsLazyPagingItems()
    val lf = LocalLifecycleOwner.current.lifecycle
    val context = LocalContext.current
    val topAppBarState = LocalTopAppBarState.current
    var topBarHeightOffset by rememberSaveable {
        mutableFloatStateOf(0f)
    }
    var topBarContentOffset by rememberSaveable {
        mutableFloatStateOf(0f)
    }
    val vibrator = remember {
        context.getSystemService(Vibrator::class.java) as Vibrator
    }

    LaunchedEffect(Unit) {
        lf.repeatOnLifecycle(Lifecycle.State.STARTED) {
            interactionSource.interactions.collect {
                when (it) {
                    is SaveMoviesInteraction -> {
                        viewModel.onEvent(HomeEvent.SaveAllMovies(moviesStream.itemSnapshotList.items))
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
                        }
                    }

                    is SearchInteraction -> {
                        topBarContentOffset = topAppBarState.contentOffset
                        topBarHeightOffset = topAppBarState.heightOffset
                        onSearchIconPressed()
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        topAppBarState.contentOffset = topBarContentOffset
        topAppBarState.heightOffset = topBarHeightOffset
    }

    val padding = LocalPaddings.current.calculateTopPadding()
    LaunchedEffect(padding) {
        viewModel.updateTopPadding(max(viewModel.topPadding, padding))
    }

    val listState = rememberLazyListState()


    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    top = viewModel.topPadding,
                    bottom = WindowInsets.navigationBars
                        .asPaddingValues()
                        .calculateBottomPadding() + 16.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(
                    count = moviesStream.itemCount,
                    key = moviesStream.itemKey { it.id },
                    contentType = moviesStream.itemContentType { it::class.java }
                ) {
                    val item = moviesStream[it]
                    item?.let { movie ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .height(400.dp),
                            onClick = {
                                topBarContentOffset = topAppBarState.contentOffset
                                topBarHeightOffset = topAppBarState.heightOffset
                                onDetailPressed(movie.id)
                            }
                        ) {
                            movie.backdropPath?.let { backDrop ->
                                AsyncImage(
                                    model = "https://image.tmdb.org/t/p/w500/$backDrop",
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                )
                            }
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(16.dp)
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 8.dp)
                                    .padding(bottom = 16.dp),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Text(
                                    text = movie.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                                if (movie.isAdult) {
                                    Box(
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(
                                                    color = Color.Red.copy(alpha = 0.6f)
                                                )
                                                .padding(8.dp),
                                            text = "Adult"
                                        )
                                    }
                                }
                                Text(
                                    modifier = Modifier.padding(top = 8.dp),
                                    text = movie.overview,
                                    style = MaterialTheme.typography.bodySmall,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}