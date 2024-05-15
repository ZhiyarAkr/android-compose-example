package com.akz.cinema.ui.screen.search

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.akz.cinema.ui.util.slideInOrStandardEnter
import com.akz.cinema.ui.util.slideInOrStandardPopEnter
import com.akz.cinema.ui.util.slideOutOrStandardExit
import com.akz.cinema.ui.util.slideOutOrStandardPopExit
import kotlinx.serialization.Serializable

@Serializable
object SearchScreenRoute

fun NavGraphBuilder.searchScreenNavGraph(
    onDetailPressed: (Int) -> Unit,
) {
    composable<SearchScreenRoute>(
        exitTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideOutOrStandardExit,
        enterTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideInOrStandardEnter,
        popEnterTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideInOrStandardPopEnter,
        popExitTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideOutOrStandardPopExit,
    ) {
        SearchScreen(
            onDetailPressed = onDetailPressed
        )
    }
}

fun NavController.navigateToSearchScreen(navOptions: NavOptions? = null) =
    navigate(
        route = SearchScreenRoute,
        navOptions = navOptions
    )


fun NavDestination.isSearchScreenCurrentDestination() = hasRoute(SearchScreenRoute::class)

