package com.akz.cinema.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.akz.cinema.ui.theme.CinemaTheme

@Composable
fun ConnectivityStatusBar(
    modifier: Modifier = Modifier,
    isConnected: Boolean
) {
    val animatedBgColor by animateColorAsState(
        targetValue = if (isConnected) Color.Green.copy(
            green = 0.6f
        ) else {
            MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        },
        label = "connectivity_status_bg_color_animation",
        animationSpec = tween(
            durationMillis = 200
        )
    )

    AnimatedVisibility(
        modifier = modifier,
        visible = !isConnected,
        enter = slideIn(
            animationSpec = tween(
                durationMillis = 200,
                delayMillis = 1000
            ),
            initialOffset = {
                IntOffset(
                    x = 0,
                    y = it.height
                )
            }
        ) + expandVertically(
            animationSpec = tween(
                durationMillis = 200,
                delayMillis = 1000
            ),
            expandFrom = Alignment.Bottom
        ),
        exit = slideOut(
            animationSpec = tween(
                durationMillis = 200,
                delayMillis = 2000
            ),
            targetOffset = {
                IntOffset(
                    x = 0,
                    y = it.height
                )
            }
        ) + shrinkVertically(
            animationSpec = tween(
                durationMillis = 200,
                delayMillis = 2000
            ),
            shrinkTowards = Alignment.Bottom
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = animatedBgColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isConnected) "Back online" else "No connection",
                style = MaterialTheme.typography.bodySmall,
                color = if (isConnected) Color.LightGray else MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConnectivityStatusBarPreview() {
    CinemaTheme(
        darkTheme = true,
        dynamicColor = false
    ) {
        ConnectivityStatusBar(isConnected = false)
    }
}