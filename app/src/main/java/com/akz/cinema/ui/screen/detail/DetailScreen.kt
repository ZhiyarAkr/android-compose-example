package com.akz.cinema.ui.screen.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.akz.cinema.LocalPaddings
import com.akz.cinema.R
import com.akz.cinema.util.RemoteImageSize
import com.akz.cinema.util.getUriForLocalDetailImage
import com.akz.cinema.util.getUriForRemoteImage
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    viewModel: DetailScreenViewModel = hiltViewModel(),
    movieId: Int?,
    onBackPressed: () -> Unit
) {
    val movie by viewModel.movieDetail.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val paddingValues = LocalPaddings.current
    val context = LocalContext.current

    val showScrollIcon by remember {
        derivedStateOf {
            scrollState.value == 0 && scrollState.canScrollForward
        }
    }

    var topPadding by remember {
        mutableStateOf(0.dp)
    }

    LaunchedEffect(paddingValues) {
        topPadding = maxOf(topPadding, paddingValues.calculateTopPadding())
    }

    LaunchedEffect(Unit) {
        movieId?.let {
            viewModel.onEvent(DetailEvent.GetDetail(it))
        }
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_PAUSE) {
        viewModel.onEvent(DetailEvent.EnqueueLocalStorageWorkers)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = topPadding)
            .navigationBarsPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            movie?.let {
                AsyncImage(
                    model = if (it.isSavedInLocal) getUriForLocalDetailImage(
                        it.backdropPath,
                        context
                    ) else getUriForRemoteImage(it.backdropPath, RemoteImageSize.ImageSizeW780),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = it.title,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 3,
                        softWrap = true,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            if (viewModel.saveToLocal) {
                                viewModel.onEvent(DetailEvent.DeleteDetail(it))
                            } else {
                                viewModel.onEvent(DetailEvent.SaveDetail(it))
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (viewModel.saveToLocal) ImageVector.vectorResource(R.drawable.baseline_bookmark_24) else ImageVector.vectorResource(
                                R.drawable.baseline_bookmark_border_24
                            ),
                            contentDescription = "Bookmark",
                            tint = Color(
                                red = 1f,
                                green = 0.5f,
                                blue = 0f
                            )
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = "Overview: ${it.overview}")
                    Text(text = "Status: ${it.status}")
                    Text(text = "Budget: ${it.budget}")
                    Text(text = "Revenue: ${it.revenue}")
                    Text(text = "Release Date: ${it.releaseDate}")
                }
            }
        }

        AnimatedVisibility(
            visible = showScrollIcon,
            modifier = Modifier
                .padding(WindowInsets.navigationBars.asPaddingValues())
                .padding(32.dp)
                .align(Alignment.BottomEnd),
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 200
                )
            ),
            exit = fadeOut(
                animationSpec = tween(
                    durationMillis = 200
                )
            )
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        scrollState.animateScrollTo(
                            scrollState.maxValue,
                            animationSpec = tween(
                                durationMillis = 100,
                                easing = FastOutSlowInEasing
                            )
                        )
                    }
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_keyboard_double_arrow_down_24),
                    tint = Color.Yellow.copy(
                        green = 0.5f
                    ),
                    contentDescription = "Can be Scrolled"
                )
            }
        }
    }
}
