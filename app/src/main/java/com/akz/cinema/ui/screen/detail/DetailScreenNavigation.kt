package com.akz.cinema.ui.screen.detail


import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val DETAIL_SCREEN_ROUTE = "detail_screen/{movieId}"

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
            }
        )
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