package com.akz.cinema.ui.screen.home

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val HOME_SCREEN_ROUTE = "home_screen"

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.homeScreenNavGraph(
    onDetailPressed: (Int) -> Unit,
    onSearchIconPressed: () -> Unit,
    interactionSource: MutableInteractionSource,
    scrollBehavior: TopAppBarScrollBehavior
) {
    composable(
        route = HOME_SCREEN_ROUTE
    ) {
        HomeScreen(
            interactionSource = interactionSource,
            scrollBehavior = scrollBehavior,
            onDetailPressed = onDetailPressed,
            onSearchIconPressed = onSearchIconPressed
        )
    }
}

fun NavController.navigateToHomeScreen(navOptions: NavOptions? = null) {
    navigate(route = HOME_SCREEN_ROUTE, navOptions = navOptions)
}

fun NavDestination.isHomeScreen() = route == HOME_SCREEN_ROUTE