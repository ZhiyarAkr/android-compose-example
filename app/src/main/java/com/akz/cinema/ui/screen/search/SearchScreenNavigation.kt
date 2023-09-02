package com.akz.cinema.ui.screen.search

import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val SearchScreenRoute = "search_screen"

fun NavGraphBuilder.searchScreenNavGraph(
    onDetailPressed: (Int) -> Unit
) {
    composable(route = SearchScreenRoute) {
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