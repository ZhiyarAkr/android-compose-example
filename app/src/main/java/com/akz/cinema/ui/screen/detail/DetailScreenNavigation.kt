package com.akz.cinema.ui.screen.detail


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
data class DetailScreenRoute(
    val movieId: Int,
    val transition: Int
)

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.detailScreenNavGraph(
    onBackPressed: () -> Unit,
    sharedTransitionScope: SharedTransitionScope
) {
    composable<DetailScreenRoute>(
        exitTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideOutOrStandardExit,
        enterTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideInOrStandardEnter,
        popEnterTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideInOrStandardPopEnter,
        popExitTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideOutOrStandardPopExit,
    ) {
        DetailScreen(
            onBackPressed = onBackPressed,
            sharedTransitionScope = sharedTransitionScope,
            animatedContentScope = this
        )
    }
}

fun NavController.navigateToDetailScreen(
    movieId: Int,
    transition: Int = 1,
    navOptions: NavOptions? = null
) = navigate(DetailScreenRoute(movieId, transition), navOptions)

fun NavDestination.isDetailScreen() = hasRoute(DetailScreenRoute::class)