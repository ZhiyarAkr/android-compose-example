package com.akz.cinema.ui.screen.detail


import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.ui.graphics.TransformOrigin
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val DETAIL_SCREEN_ROUTE = "detail_screen/{movieId}?x={x}&y={y}"

fun NavGraphBuilder.detailScreenNavGraph(
    onBackPressed: () -> Unit
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
                name = "x"
            ) {
                type = NavType.FloatType
                defaultValue = -1f
            },
            navArgument(
                name = "y"
            ) {
                type = NavType.FloatType
                defaultValue = -1f
            },
        ),
        exitTransition = AnimatedContentTransitionScope<NavBackStackEntry>::exit,
        enterTransition = AnimatedContentTransitionScope<NavBackStackEntry>::enter
    ) {
        val movieId = it.arguments?.getInt("movieId")
        DetailScreen(movieId = movieId, onBackPressed = onBackPressed)
    }
}

fun NavController.navigateToDetailScreen(movieId: Int, navOptions: NavOptions? = null) {
    navigate(
        route = DETAIL_SCREEN_ROUTE.replace(
            "{movieId}",
            "$movieId"
        ),
        navOptions = navOptions
    )
}

fun NavDestination.isDetailScreen() = route == DETAIL_SCREEN_ROUTE

private fun AnimatedContentTransitionScope<NavBackStackEntry>.exit(): ExitTransition {
    return scaleOut(targetScale = 0.8f) + fadeOut()
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.enter(): EnterTransition {
    return if (targetState.destination.isDetailScreen()) {
        val x = initialState.arguments?.getFloat("x") ?: -1f
        val y = initialState.arguments?.getFloat("y") ?: -1f
        if (x == -1f || y == -1f) {
            scaleIn(initialScale = 0.8f) + fadeIn()
        } else {
            scaleIn(
                initialScale = 0.2f,
                transformOrigin = TransformOrigin(x, y)
            ) + fadeIn()
        }
    } else {
        scaleIn(initialScale = 0.8f) + fadeIn()
    }
}