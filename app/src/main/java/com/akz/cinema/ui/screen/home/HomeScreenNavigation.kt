package com.akz.cinema.ui.screen.home

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.akz.cinema.ui.util.slideInOrStandardEnter
import com.akz.cinema.ui.util.slideInOrStandardPopEnter
import com.akz.cinema.ui.util.slideOutOrStandardExit
import com.akz.cinema.ui.util.slideOutOrStandardPopExit

const val HOME_SCREEN_ROUTE = "home_screen"

fun NavGraphBuilder.homeScreenNavGraph(
    onDetailPressed: (Int) -> Unit,
) {
    composable(
        route = HOME_SCREEN_ROUTE,
        exitTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideOutOrStandardExit,
        enterTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideInOrStandardEnter,
        popEnterTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideInOrStandardPopEnter,
        popExitTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideOutOrStandardPopExit,
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

