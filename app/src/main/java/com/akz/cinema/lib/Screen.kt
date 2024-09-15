package com.akz.cinema.lib

import androidx.navigation.NavDestination
import com.akz.cinema.ui.screen.detail.isDetailScreen
import com.akz.cinema.ui.screen.home.isHomeScreen
import com.akz.cinema.ui.screen.saved.isSavedCurrentDestination
import com.akz.cinema.ui.screen.search.isSearchScreenCurrentDestination

enum class Screen(
    val isActiveScreen: NavDestination.() -> Boolean,
    val hasTopBar: Boolean = false,
    val hasNavigationBar: Boolean = true,
    val hasBackBtn: Boolean = false,
    val topAppBarScrollable: Boolean = false,
    val topBarTitle: String? = null
) {
    Home(
        NavDestination::isHomeScreen,
    ),
    Detail(
        NavDestination::isDetailScreen,
        hasNavigationBar = false
    ),
    Search(
        NavDestination::isSearchScreenCurrentDestination,
        hasNavigationBar = true,
    ),
    Saved(
        NavDestination::isSavedCurrentDestination,
        hasNavigationBar = true
    )
}

fun NavDestination.getScreen() : Screen {
    Screen.entries.forEach {
        with(it) {
            if(isActiveScreen()) return it
        }
    }
    return Screen.Home
}

