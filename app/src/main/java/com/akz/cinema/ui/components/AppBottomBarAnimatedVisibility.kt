package com.akz.cinema.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AppBottomBarAnimatedVisibility(
    modifier: Modifier = Modifier,
    visible: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = slideInVertically(
            initialOffsetY = {
                it
            }
        ) + expandVertically(expandFrom = Alignment.Bottom),
        exit = slideOutVertically(
            targetOffsetY = {
                it
            }
        ) + shrinkVertically(shrinkTowards = Alignment.Bottom)
    ) {
        content()
    }
}