package com.akz.cinema.lib

import android.content.res.Resources
import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import com.akz.cinema.R
import com.akz.cinema.ui.screen.home.isHomeScreen
import com.akz.cinema.ui.screen.home.navigateToHomeScreen
import com.akz.cinema.ui.screen.saved.isSavedCurrentDestination
import com.akz.cinema.ui.screen.saved.navigateToSavedScreen
import com.akz.cinema.ui.screen.search.isSearchScreenCurrentDestination
import com.akz.cinema.ui.screen.search.navigateToSearchScreen

enum class NavigationItem(
    val title: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unSelectedIcon: Int,
    val isCurrentDestination: NavDestination.() -> Boolean,
    val navigateTo: NavController.(NavOptions) -> Unit,
    val navOptions: NavOptions
) {
    Home(
        title = "Home",
        selectedIcon = R.drawable.baseline_home_24,
        unSelectedIcon = R.drawable.outline_home_24,
        isCurrentDestination = NavDestination::isHomeScreen,
        navigateTo = NavController::navigateToHomeScreen,
        navOptions = NavOptions.Builder()
            .setRestoreState(true)
            .setLaunchSingleTop(true)
            .setPopUpTo("home_screen", false)
            .build()
    ),
    Search(
        title = "Search",
        selectedIcon = R.drawable.baseline_search_24,
        unSelectedIcon = R.drawable.baseline_search_24,
        isCurrentDestination = NavDestination::isSearchScreenCurrentDestination,
        navigateTo = NavController::navigateToSearchScreen,
        navOptions = NavOptions.Builder()
            .setRestoreState(true)
            .setPopUpTo("home_screen", false)
            .build()
    ),
    Saved(
        title = "Saved",
        selectedIcon = R.drawable.baseline_bookmark_24,
        unSelectedIcon = R.drawable.baseline_bookmark_border_24,
        isCurrentDestination = NavDestination::isSavedCurrentDestination,
        navigateTo = NavController::navigateToSavedScreen,
        navOptions = NavOptions.Builder()
            .setRestoreState(true)
            .setPopUpTo("home_screen", false)
            .build()
    )
}