package com.akz.cinema.ui.screen.saved

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.currentStateAsState
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.akz.cinema.data.detail.MovieDetail
import com.akz.cinema.util.getUriForLocalDetailImage

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SavedScreen(
    viewModel: SavedScreenViewModel = hiltViewModel(),
    onDetail: (Int) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val movieDetails by viewModel.localMovieDetails.collectAsStateWithLifecycle()
    SavedScreenContent(movieDetails, onDetail, sharedTransitionScope, animatedContentScope)
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SavedScreenContent(
    movies: List<MovieDetail>,
    onDetail: (Int) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        if (movies.isNotEmpty()) {
            SavedScreenLazyColumn(
                movies = movies,
                onDetail = onDetail,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
            )
        } else {
            Text(
                text = "No Movies Saved",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(top = 64.dp)
                    .statusBarsPadding()
            )
        }
        with(sharedTransitionScope) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .renderInSharedTransitionScopeOverlay(
                        zIndexInOverlay = 10f
                    )
                    .background(MaterialTheme.colorScheme.background)
                    .statusBarsPadding()
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SavedScreenLazyColumn(
    modifier: Modifier = Modifier,
    movies: List<MovieDetail>,
    onDetail: (Int) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(
            top = 32.dp,
            bottom = 100.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        ),
    ) {
        items(
            items = movies,
            key = { it.id }
        ) {
            MovieItem(
                movie = it,
                onClick = onDetail,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope
            )
        }
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun MovieItem(
    modifier: Modifier = Modifier,
    movie: MovieDetail,
    onClick: (Int) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val context = LocalContext.current
    val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateAsState()

    SharedTransitionScope {
        Card(
            modifier = modifier
                .fillMaxWidth(0.9f)
                .height(400.dp),
            shape = RoundedCornerShape(16.dp),
            onClick = {
                if (lifecycleState.isAtLeast(Lifecycle.State.RESUMED)) {
                    onClick(movie.id)
                }
            },
        ) {
            with(sharedTransitionScope) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(
                            getUriForLocalDetailImage(
                                backdrop = movie.backdropPath,
                                context = context
                            )
                        )
                        .crossfade(true)
                        .placeholderMemoryCacheKey("image_${movie.id}")
                        .memoryCacheKey("image_${movie.id}")
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .sharedElement(
                            rememberSharedContentState(key = "image_${movie.id}"),
                            animatedVisibilityScope = animatedContentScope,
//                            enter = EnterTransition.None,
//                            exit = ExitTransition.None,
//                            zIndexInOverlay = 1f
                        )
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
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
                with(sharedTransitionScope) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .sharedBounds(
                                rememberSharedContentState(key = "title_${movie.id}"),
                                animatedContentScope
                            )
                    )
                }
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