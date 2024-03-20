package com.akz.cinema.ui.screen.home

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val HOME_SCREEN_ROUTE = "home_screen"


fun NavGraphBuilder.homeScreenNavGraph(
    onDetailPressed: (Int) -> Unit,
) {
    composable(
        route = HOME_SCREEN_ROUTE,
        enterTransition = AnimatedContentTransitionScope<NavBackStackEntry>::enter,
        exitTransition = AnimatedContentTransitionScope<NavBackStackEntry>::exit
    ) {
        HomeScreen(
            onDetailPressed = onDetailPressed,
        )
    }
}

fun NavController.navigateToHomeScreen(navOptions: NavOptions? = null) {
    navigate(route = HOME_SCREEN_ROUTE, navOptions = navOptions)
}

fun NavDestination.isHomeScreen() = route == HOME_SCREEN_ROUTE

private fun AnimatedContentTransitionScope<NavBackStackEntry>.exit(): ExitTransition {
    return scaleOut(targetScale = 0.8f) + fadeOut()
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.enter(): EnterTransition {
    return scaleIn(initialScale = 0.8f) + fadeIn()
}