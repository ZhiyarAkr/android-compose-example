package com.akz.cinema.ui.components

import androidx.compose.animation.core.spring
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlin.properties.Delegates

enum class DragValue { Center, End }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeToReveal(
    modifier: Modifier = Modifier,
    state: SwipeToRevealState,
    onOffset: ((SwipeToRevealState) -> Unit)? = null,
    onBackgroundEndPressed: () -> Unit,
    backgroundEnd: @Composable () -> Unit,
    foreground: @Composable () -> Unit
) {
    val layoutDirection = LocalLayoutDirection.current

    val isRtl = remember(layoutDirection) { layoutDirection == LayoutDirection.Rtl }


    SideEffect {
        state.state.updateAnchors(state.anchors)
    }

    LaunchedEffect(state.isDragged.value) {
        if (state.isDragged.value) {
            onOffset?.invoke(state)
        }
    }

    Box(
        modifier = modifier.wrapContentSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .zIndex(0f)
                .matchParentSize(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(state.threshHold)
                    .align(Alignment.CenterEnd)
                    .clickable {
                        onBackgroundEndPressed()
                    }
            ) {
                backgroundEnd()
            }
        }
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        state
                            .requireOffset()
                            .toInt(), 0
                    )
                }
                .anchoredDraggable(
                    state = state.state,
                    orientation = Orientation.Horizontal,
                    reverseDirection = isRtl
                )
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            foreground()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
class SwipeToRevealState {
    lateinit var anchors: DraggableAnchors<DragValue>
    lateinit var state: AnchoredDraggableState<DragValue>
    var threshHold by Delegates.notNull<Float>()
    val isDragged by lazy { derivedStateOf { state.offset != 0f } }
    suspend fun reset() = state.animateTo(DragValue.Center)
    fun requireOffset() = state.requireOffset()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberSwipeToRevealState(
    threshHold: Float,
): SwipeToRevealState {
    val density = LocalDensity.current
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val screenWidth =
        remember(screenWidthDp, density) { with(density) { screenWidthDp.toPx() } }
    val dragAnchors = remember {
        DraggableAnchors {
            DragValue.Center at 0f
            DragValue.End at (-screenWidth * threshHold)
        }
    }
    val dragState = remember {
        AnchoredDraggableState(
            initialValue = DragValue.Center,
            anchors = dragAnchors,
            positionalThreshold = {
                it * 0.5f
            },
            velocityThreshold = {
                with(density) { 100.dp.toPx() }
            },
            snapAnimationSpec = spring(),
            decayAnimationSpec = splineBasedDecay(density)
        )
    }
    return remember {
        SwipeToRevealState().apply {
            anchors = dragAnchors
            state = dragState
            this.threshHold = threshHold
        }
    }
}