package com.akz.cinema.ui.screen.search

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

const val SearchScreenRoute = "search_screen"

fun NavGraphBuilder.searchScreenNavGraph(
    onDetailPressed: (Int) -> Unit,
) {
    composable(
        route = SearchScreenRoute,
        enterTransition = AnimatedContentTransitionScope<NavBackStackEntry>::enter,
        exitTransition = AnimatedContentTransitionScope<NavBackStackEntry>::exit,
    ) {
        SearchScreen(
            onDetailPressed = onDetailPressed
        )
    }
}

fun NavController.navigateToSearchScreen(navOptions: NavOptions? = null) {
    navigate(
        route = SearchScreenRoute,
        navOptions = navOptions
    )
}

fun NavDestination.isSearchScreenCurrentDestination() = route == SearchScreenRoute

private fun AnimatedContentTransitionScope<NavBackStackEntry>.exit(): ExitTransition {
    return scaleOut(targetScale = 0.8f) + fadeOut()
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.enter(): EnterTransition {
    return scaleIn(initialScale = 0.8f) + fadeIn()
}