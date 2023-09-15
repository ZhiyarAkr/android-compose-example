package com.akz.cinema.ui.components

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    interactionSource: MutableInteractionSource,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        actions = {
            IconButton(
                onClick = {
                    scope.launch {
                        interactionSource.emit(SaveMoviesInteraction())
                    }
                },
                enabled = false
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
    )
}

class SaveMoviesInteraction : Interaction
class SearchInteraction : Interaction