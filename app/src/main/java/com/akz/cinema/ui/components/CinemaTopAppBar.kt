package com.akz.cinema.ui.components

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.akz.cinema.R
import com.akz.cinema.lib.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CinemaTopAppBar(
    modifier: Modifier = Modifier,
    screen: Screen,
    interactionSource: MutableInteractionSource,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (screen.hasBackBtn) {
                IconButton(onClick = { scope.launch { interactionSource.emit(BackPressInteraction()) } }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Navigate back"
                    )
                }
            }
        },
        title = {
            screen.topBarTitle?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = {
            when(screen) {
                Screen.Home -> HomeTopBarContents(interactionSource = interactionSource)
                Screen.Detail -> {}
                Screen.Search -> {}
            }
        }
    )
}

class SaveMoviesInteraction : Interaction
class SearchInteraction : Interaction
class BackPressInteraction : Interaction

@Composable
private fun HomeTopBarContents(
    interactionSource: MutableInteractionSource
) {
    val scope = rememberCoroutineScope()
    IconButton(
        onClick = {
            scope.launch {
                interactionSource.emit(SaveMoviesInteraction())
            }
        }
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_bookmark_border_24),
            contentDescription = "bookmark"
        )
    }
    IconButton(
        onClick = {
            scope.launch {
                interactionSource.emit(SearchInteraction())
            }
        }
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "search"
        )
    }
}