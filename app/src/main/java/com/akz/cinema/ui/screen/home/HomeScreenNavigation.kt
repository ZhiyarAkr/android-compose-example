package com.akz.cinema.ui.screen.home

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
object HomeScreenRoute

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.homeScreenNavGraph(
    onDetailPressed: (Int) -> Unit,
    sharedTransitionScope: SharedTransitionScope
) {
    composable<HomeScreenRoute>(
        exitTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideOutOrStandardExit,
        enterTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideInOrStandardEnter,
        popEnterTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideInOrStandardPopEnter,
        popExitTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideOutOrStandardPopExit,
    ) {
        HomeScreen(
            onDetailPressed = onDetailPressed,
            sharedTransitionScope = sharedTransitionScope,
            animatedContentScope = this
        )
    }
}

fun NavController.navigateToHomeScreen(navOptions: NavOptions? = null) =
    navigate(route = HomeScreenRoute, navOptions = navOptions)


fun NavDestination.isHomeScreen() = hasRoute(HomeScreenRoute::class)

