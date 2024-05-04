package com.akz.cinema.ui.screen.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.akz.cinema.LocalHideNavBar
import com.akz.cinema.LocalPaddings
import com.akz.cinema.ui.components.Skeleton
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel = hiltViewModel(),
    onDetailPressed: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = LocalPaddings.current.calculateBottomPadding())
    ) {
        val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
        val recentCount by viewModel.recentMoviesCount.collectAsStateWithLifecycle()
        val keyboardController = LocalSoftwareKeyboardController.current
        var hideNavbar by LocalHideNavBar.current
        val context = LocalContext.current

        LaunchedEffect(viewModel.isSearchBarActive) {
            hideNavbar = viewModel.isSearchBarActive
        }
        DisposableEffect(Unit) {
            onDispose {
                hideNavbar = false
            }
        }

        LaunchedEffect(viewModel.searchQuery) {
            delay(1000)
            viewModel.searchMovies()
        }

        val onActiveChange = { it: Boolean ->
            viewModel.updateIsSearchBarActive(it)
        }
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = viewModel.searchQuery,
                    onQueryChange = {
                        viewModel.updateSearchQuery(it)
                    },
                    onSearch = {
                        viewModel.onEvent(
                            SearchEvent.HideKeyboard(keyboardController)
                        )
                    },
                    expanded = viewModel.isSearchBarActive,
                    onExpandedChange = onActiveChange,
                    enabled = true,
                    placeholder = {
                        Text(text = "Search movies...")
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    },
                    trailingIcon = {
                        if (viewModel.isSearchBarActive) {
                            IconButton(
                                onClick = {
                                    if (viewModel.searchQuery.isEmpty()) {
                                        viewModel.updateIsSearchBarActive(false)
                                    } else {
                                        viewModel.updateSearchQuery("")
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close search"
                                )
                            }
                        }
                    },
                    interactionSource = null,
                )
            },
            expanded = viewModel.isSearchBarActive,
            onExpandedChange = onActiveChange,
            modifier = Modifier.align(Alignment.TopCenter),
            windowInsets = SearchBarDefaults.windowInsets,
            content = {
                val listItemModifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                AnimatedVisibility(
                    visible = viewModel.isHistoryBeingServed,
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
                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Recent: ",
                            style = MaterialTheme.typography.titleLarge,
                        )
                        recentCount?.let {
                            Text(
                                text = "$it found",
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding(),
                ) {
                    items(items = searchResults) { movie ->
                        ListItem(
                            modifier = listItemModifier.clickable {
                                viewModel.onEvent(SearchEvent.SaveToRecent(movie, context))
                                onDetailPressed(movie.id)
                            },
                            headlineContent = {
                                Text(
                                    text = movie.title,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            leadingContent = {
                                movie.backdropPath?.let { backDrop ->
                                    val path =
                                        if (movie.isImagePathAbsolute) backDrop else "https://image.tmdb.org/t/p/w500/$backDrop"
                                    AsyncImage(
                                        model = path,
                                        contentDescription = "Movie Photo",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .width(150.dp)
                                            .fillMaxHeight()
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                } ?: Skeleton(
                                    modifier = Modifier
                                        .width(150.dp)
                                        .height(100.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = movie.overview,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        )
                    }
                }
            },
        )
    }
}