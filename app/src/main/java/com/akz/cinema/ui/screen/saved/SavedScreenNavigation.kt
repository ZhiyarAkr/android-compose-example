package com.akz.cinema.ui.screen.saved

import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val SavedScreenRoute = "saved_screen"
fun NavGraphBuilder.savedScreenGraph(

) {
    composable(
        route = SavedScreenRoute
    ) {
        SavedScreen()
    }
}

fun NavDestination.isSavedCurrentDestination() = route == SavedScreenRoute
fun NavController.navigateToSavedScreen(navOptions: NavOptions? = null) {
    navigate(SavedScreenRoute, navOptions)
}