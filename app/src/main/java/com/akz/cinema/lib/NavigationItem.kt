package com.akz.cinema.lib

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.akz.cinema.ui.screen.home.navigateToHomeScreen
import com.akz.cinema.ui.screen.search.navigateToSearchScreen

enum class NavigationItem(
    val title: String,
    val selectedImageVector: ImageVector,
    val unSelectedImageVector: ImageVector,
    val navigateTo: NavController.(NavOptions) -> Unit,
    val navOptions: NavOptions
) {
    Home(
        title = "Home",
        selectedImageVector = Icons.Filled.Home,
        unSelectedImageVector = Icons.Outlined.Home,
        navigateTo = NavController::navigateToHomeScreen,
        navOptions = NavOptions.Builder()
            .setRestoreState(true)
            .setLaunchSingleTop(true)
            .setPopUpTo("home_screen", false)
            .build()
    ),
    Search(
        title = "Search",
        selectedImageVector = Icons.Default.Search,
        unSelectedImageVector = Icons.Outlined.Search,
        navigateTo = NavController::navigateToSearchScreen,
        navOptions = NavOptions.Builder()
            .setRestoreState(true)
            .setPopUpTo("home_screen", false)
            .build()
    )
}