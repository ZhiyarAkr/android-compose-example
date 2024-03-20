package com.akz.cinema.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.areNavigationBarsVisible
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AppBottomBar(
    modifier: Modifier = Modifier,
    isConnected: Boolean,
    isNavBarVisible: Boolean,
    navController: NavController
) {
    val connectivityModifier = if (WindowInsets.areNavigationBarsVisible && !isNavBarVisible) {
        Modifier.navigationBarsPadding()
    } else {
        Modifier
    }
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        ConnectivityStatusBar(
            modifier = connectivityModifier,
            isConnected = isConnected
        )
        AppBottomBarAnimatedVisibility(
            visible = isNavBarVisible
        ) {
            CinemaNavigationBar(navController = navController)
        }
    }
}