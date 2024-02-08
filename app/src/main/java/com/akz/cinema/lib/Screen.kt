package com.akz.cinema.lib

import androidx.navigation.NavDestination
import com.akz.cinema.ui.screen.detail.isDetailScreen
import com.akz.cinema.ui.screen.home.isHomeScreen
import com.akz.cinema.ui.screen.search.isSearchScreenCurrentDestination

enum class Screen(
    val isActiveScreen: NavDestination.() -> Boolean,
    val hasTopBar: Boolean = false,
    val hasBackBtn: Boolean = false,
    val topAppBarScrollable: Boolean = false,
    val topBarTitle: String? = null
) {
    Home(
        NavDestination::isHomeScreen,
        hasTopBar = true,
        topBarTitle = "Home",
        topAppBarScrollable = true
    ),
    Detail(
        NavDestination::isDetailScreen,
        hasTopBar = true,
        hasBackBtn = true
    ),
    Search(
        NavDestination::isSearchScreenCurrentDestination,
        hasTopBar = true,
        hasBackBtn = true,
        topBarTitle = "Search"
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

