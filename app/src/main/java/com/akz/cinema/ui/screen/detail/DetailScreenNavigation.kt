package com.akz.cinema.ui.screen.detail


import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.akz.cinema.ui.util.slideInOrStandardEnter
import com.akz.cinema.ui.util.slideInOrStandardPopEnter
import com.akz.cinema.ui.util.slideOutOrStandardExit
import com.akz.cinema.ui.util.slideOutOrStandardPopExit

const val DETAIL_SCREEN_ROUTE = "detail_screen/{movieId}?transition={transition}"

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.detailScreenNavGraph(
    onBackPressed: () -> Unit,
    sharedTransitionScope: SharedTransitionScope
) {
    composable(
        route = DETAIL_SCREEN_ROUTE,
        arguments = listOf(
            navArgument(
                name = "movieId"
            ) {
                type = NavType.IntType
            },
            navArgument(
                name = "transition"
            ) {
                type = NavType.IntType
                defaultValue = 0
            }
        ),
        exitTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideOutOrStandardExit,
        enterTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideInOrStandardEnter,
        popEnterTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideInOrStandardPopEnter,
        popExitTransition = AnimatedContentTransitionScope<NavBackStackEntry>::slideOutOrStandardPopExit,
    ) {
        val movieId = it.arguments?.getInt("movieId")
        DetailScreen(
            movieId = movieId,
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
) {
    navigate(
        route = DETAIL_SCREEN_ROUTE.replace(
            "{movieId}",
            "$movieId"
        ).replace(
            "{transition}",
            "$transition"
        ),
        navOptions = navOptions
    )
}

fun NavDestination.isDetailScreen() = route == DETAIL_SCREEN_ROUTE