package com.akz.cinema.ui.screen.saved

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.akz.cinema.util.slideInOrStandardEnter
import com.akz.cinema.util.slideInOrStandardPopEnter
import com.akz.cinema.util.slideOutOrStandardExit
import com.akz.cinema.util.slideOutOrStandardPopExit

const val SavedScreenRoute = "saved_screen"
fun NavGraphBuilder.savedScreenGraph(
    onDetail: (Int) -> Unit
) {
    composable(
        route = SavedScreenRoute,

        exitTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideOutOrStandardExit,
        enterTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideInOrStandardEnter,
        popEnterTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideInOrStandardPopEnter,
        popExitTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideOutOrStandardPopExit,
    ) {
        SavedScreen(onDetail = onDetail)
    }
}

fun NavDestination.isSavedCurrentDestination() = route == SavedScreenRoute
fun NavController.navigateToSavedScreen(navOptions: NavOptions? = null) {
    navigate(SavedScreenRoute, navOptions)
}