package com.akz.cinema.util

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

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideInOrStandardEnter(): EnterTransition {
    val transition = targetState.arguments?.getInt("transition")
    transition?.let {
        if (it == 1) {
            return slideInHorizontally(
                initialOffsetX = {offset ->
                    offset
                }
            )
        } else {
            null
        }
    } ?: return scaleIn(initialScale = 0.8f) + fadeIn()
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideInOrStandardPopEnter(): EnterTransition {
    val transition = initialState.arguments?.getInt("transition")
    transition?.let {
        if (it == 1) {
            return slideInHorizontally(
                initialOffsetX = {offset ->
                    -offset
                }
            )
        } else {
            null
        }
    } ?: return scaleIn(initialScale = 0.8f) + fadeIn()
}
fun AnimatedContentTransitionScope<NavBackStackEntry>.slideOutOrStandardExit(): ExitTransition {
    val transition = targetState.arguments?.getInt("transition")
    transition?.let {
        if (it == 1) {
            return slideOutHorizontally(
                targetOffsetX = {offset ->
                    -offset
                }
            )
        } else null
    } ?: return scaleOut(targetScale = 0.8f) + fadeOut()
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideOutOrStandardPopExit(): ExitTransition {
    val transition = initialState.arguments?.getInt("transition")
    transition?.let {
        if (it == 1) {
            return slideOutHorizontally(
                targetOffsetX = {offset ->
                    offset
                }
            )
        } else null
    } ?: return scaleOut(targetScale = 0.8f) + fadeOut()
}