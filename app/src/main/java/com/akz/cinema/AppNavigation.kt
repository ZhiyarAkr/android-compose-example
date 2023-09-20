package com.akz.cinema

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.akz.cinema.ui.components.ConnectivityStatusBar
import com.akz.cinema.ui.components.HomeTopAppBar
import com.akz.cinema.ui.screen.detail.detailScreenNavGraph
import com.akz.cinema.ui.screen.detail.navigateToDetailScreen
import com.akz.cinema.ui.screen.home.HOME_SCREEN_ROUTE
import com.akz.cinema.ui.screen.home.homeScreenNavGraph
import com.akz.cinema.ui.screen.home.isHomeScreen
import com.akz.cinema.ui.screen.search.navigateToSearchScreen
import com.akz.cinema.ui.screen.search.searchScreenNavGraph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    viewModel: MainActivityViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val interactionSource = remember { MutableInteractionSource() }

    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()
    val showConnectivityStatus by viewModel.showConnectivityStatus.collectAsStateWithLifecycle()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val showTopAppBar by remember(backStackEntry) {
        derivedStateOf {
            when {
                backStackEntry?.destination?.isHomeScreen() == true -> true
                else -> false
            }
        }
    }



    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            ConnectivityStatusBar(
                show = showConnectivityStatus,
                isConnected = isConnected
            )
        },
        topBar = {
            if (showTopAppBar) {
                HomeTopAppBar(
                    title = "Home",
                    interactionSource = interactionSource,
                    scrollBehavior = scrollBehavior
                )
            }
        }
    ) { paddings ->
        CompositionLocalProvider(
            LocalPaddings provides paddings
        ) {
            NavHost(
                navController = navController,
                startDestination = HOME_SCREEN_ROUTE
            ) {
                homeScreenNavGraph(
                    onDetailPressed = { movieId ->
                        navController.navigateToDetailScreen(movieId)
                    },
                    onSearchIconPressed = {
                        navController.navigateToSearchScreen()
                    },
                    interactionSource = interactionSource,
                    scrollBehavior = scrollBehavior
                )

                detailScreenNavGraph(
                    onBackPressed = navController::popBackStack
                )
                searchScreenNavGraph(
                    onDetailPressed = { movieId ->
                        navController.navigateToDetailScreen(movieId)
                    },
                    onBackPressed = navController::popBackStack
                )
            }
        }
    }
}

val LocalPaddings = compositionLocalOf { PaddingValues(0.dp) }