package com.akz.cinema.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.akz.cinema.lib.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CinemaTopAppBar(
    modifier: Modifier = Modifier,
    screen: Screen,
    onBackPressed: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = screen.hasTopBar,
        enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { -it })
    ) {
        val density = LocalDensity.current
        val anim = remember {
            Animatable(with(density) { 50.dp.toPx() }, Float.VectorConverter)
        }
        val anim2 = remember {
            Animatable(0f, Float.VectorConverter)
        }
        LaunchedEffect(Unit) {
            launch {
                anim.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 400,
                        delayMillis = 200,
                        easing = FastOutSlowInEasing
                    )
                )
            }
            launch {
                anim2.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 400,
                        delayMillis = 200
                    )
                )
            }
        }
        TopAppBar(
            navigationIcon = {
                if (screen.hasBackBtn) {
                    IconButton(
                        modifier = Modifier.graphicsLayer {
                            translationX = anim.value
                            alpha = anim2.value
                        },
                        onClick = onBackPressed
                    ) {
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
                when (screen) {
                    Screen.Home -> {}
                    Screen.Detail -> {}
                    Screen.Search -> {}
                    Screen.Saved -> {}
                }
            }
        )
    }
}