package com.akz.cinema.ui.util

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import com.akz.cinema.ui.screen.detail.DetailScreenRoute
import com.akz.cinema.ui.screen.detail.isDetailScreen

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideInOrStandardEnter(): EnterTransition {
    if (targetState.destination.isDetailScreen()) {
        val transition = targetState.toRoute<DetailScreenRoute>().transition
        if (transition == 1) {
            return slideInHorizontally(
                initialOffsetX = { offset ->
                    offset
                }
            ) + fadeIn()
        }
    }
    return scaleIn(initialScale = 0.8f) + fadeIn()
}


fun AnimatedContentTransitionScope<NavBackStackEntry>.slideInOrStandardPopEnter(): EnterTransition {
    if (initialState.destination.isDetailScreen()) {
        val transition = initialState.toRoute<DetailScreenRoute>().transition
        if (transition == 1) {
            return slideInHorizontally(
                initialOffsetX = { offset ->
                    -offset
                }
            ) + fadeIn()
        }
    }
    return scaleIn(initialScale = 0.8f) + fadeIn()
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideOutOrStandardExit(): ExitTransition {
    if (targetState.destination.isDetailScreen()) {
        val transition = targetState.toRoute<DetailScreenRoute>().transition
        if (transition == 1) {
            return slideOutHorizontally(
                targetOffsetX = { offset ->
                    -offset
                }
            ) + fadeOut()
        }
    }
    return scaleOut(targetScale = 0.8f) + fadeOut()
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideOutOrStandardPopExit(): ExitTransition {
    if (initialState.destination.isDetailScreen()) {
        val transition = initialState.toRoute<DetailScreenRoute>().transition
        if (transition == 1) {
            return slideOutHorizontally(
                targetOffsetX = { offset ->
                    offset
                }
            ) + fadeOut()
        }
    }
    return scaleOut(targetScale = 0.8f) + fadeOut()
}