package com.akz.cinema.ui.screen.detail

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.currentStateAsState
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.akz.cinema.LocalCanGoBack
import com.akz.cinema.LocalPaddings
import com.akz.cinema.R
import com.akz.cinema.util.RemoteImageSize
import com.akz.cinema.util.formatCurrency
import com.akz.cinema.util.getUriForLocalDetailImage
import com.akz.cinema.util.getUriForRemoteImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailScreen(
    viewModel: DetailScreenViewModel = hiltViewModel(),
    movieId: Int?,
    onBackPressed: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val movie by viewModel.movieDetail.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val paddingValues = LocalPaddings.current
    val context = LocalContext.current
    val density = LocalDensity.current
    var canGoBack by LocalCanGoBack.current
    val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateAsState()

    val showScrollIcon by remember {
        derivedStateOf {
            scrollState.value == 0 && scrollState.canScrollForward
        }
    }

    var topPadding by remember {
        mutableStateOf(0.dp)
    }

    BackHandler(enabled = !lifecycleState.isAtLeast(Lifecycle.State.RESUMED)) {

    }

    LaunchedEffect(lifecycleState) {
        if (lifecycleState.isAtLeast(Lifecycle.State.RESUMED)) {
            canGoBack = true
        }
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

    val imageEnterAnimation = remember {
        Animatable(with(density) { 64.dp.roundToPx() }, Int.VectorConverter)
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
                with(sharedTransitionScope) {
                    AsyncImage(
                        model = if (it.isSavedInLocal) ImageRequest.Builder(context)
                            .data(getUriForLocalDetailImage(it.backdropPath, context))
                            .memoryCacheKey("image_${it.id}")
                            .placeholderMemoryCacheKey("image_${it.id}")
                            .build() else ImageRequest.Builder(context)
                            .data(
                                getUriForRemoteImage(
                                    it.backdropPath,
                                    RemoteImageSize.ImageSizeW780
                                )
                            )
                            .memoryCacheKey("image_${it.id}")
                            .placeholderMemoryCacheKey("image_${it.id}")
                            .build(),
                        contentDescription = null,
                        onSuccess = {
                            scope.launch {
                                imageEnterAnimation.animateTo(
                                    targetValue = 0,
                                    animationSpec = tween(
                                        delayMillis = 300,
                                        durationMillis = 100,
                                        easing = androidx.compose.animation.core.EaseInExpo
                                    )
                                )
                            }
                        },
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .offset {
                                IntOffset(
                                    x = imageEnterAnimation.value,
                                    y = 0
                                )
                            }
                            .sharedElement(
                                rememberSharedContentState(key = "image_${it.id}"),
                                animatedContentScope,
//                                enter = EnterTransition.None,
//                                exit = fadeOut(),
                            )
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    with(sharedTransitionScope) {
                        Text(
                            text = it.title,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 3,
                            softWrap = true,
                            modifier = Modifier
                                .weight(1f)
                                .sharedBounds(
                                    rememberSharedContentState("title_${it.id}"),
                                    animatedContentScope
                                )
                        )
                    }
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
                    Text(text = "Budget: ${it.budget.formatCurrency()}")
                    Text(text = "Revenue: ${it.revenue.formatCurrency()}")
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
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Black.copy( alpha = 0.3f)
                ),
                onClick = {
                    scope.launch {
                        scrollState.animateScrollTo(
                            scrollState.maxValue
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
