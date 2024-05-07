package com.akz.cinema

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.akz.cinema.lib.Screen
import com.akz.cinema.lib.getScreen
import com.akz.cinema.ui.components.AppBottomBar
import com.akz.cinema.ui.components.CinemaTopAppBar
import com.akz.cinema.ui.screen.detail.detailScreenNavGraph
import com.akz.cinema.ui.screen.detail.navigateToDetailScreen
import com.akz.cinema.ui.screen.home.HOME_SCREEN_ROUTE
import com.akz.cinema.ui.screen.home.homeScreenNavGraph
import com.akz.cinema.ui.screen.saved.savedScreenGraph
import com.akz.cinema.ui.screen.search.searchScreenNavGraph


val LocalHideNavBar = compositionLocalOf {
    mutableStateOf(false)
}

val LocalCanGoBack = compositionLocalOf {
    mutableStateOf(false)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavigation(
    viewModel: MainActivityViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    var screen by remember { mutableStateOf(Screen.Home) }
    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()
    val topAppBarState = rememberTopAppBarState()
    val hideNavBar = remember { mutableStateOf(false) }
    val navBarVisible by remember {
        derivedStateOf {
            !hideNavBar.value && screen.hasNavigationBar
        }
    }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val canGoBack = remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topAppBarState,
        canScroll = {
            screen.topAppBarScrollable
        }
    )
    navController.addOnDestinationChangedListener { _, _, _ ->
        canGoBack.value = false
    }

    LaunchedEffect(backStackEntry) {
        screen = backStackEntry?.destination?.getScreen() ?: Screen.Home
        topAppBarState.heightOffset = 0f
        topAppBarState.contentOffset = 0f
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = {
            AppBottomBar(
                navController = navController,
                isConnected = isConnected,
                isNavBarVisible = navBarVisible
            )
        },
        topBar = {
            CompositionLocalProvider(
                LocalCanGoBack provides canGoBack
            ) {
                CinemaTopAppBar(
                    screen = screen,
                    onBackPressed = {
                        if (canGoBack.value) {
                            navController.popBackStack()
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        }
    ) { paddings ->
        CompositionLocalProvider(
            LocalPaddings provides paddings,
            LocalTopAppBarState provides topAppBarState,
            LocalHideNavBar provides hideNavBar,
            LocalCanGoBack provides canGoBack
        ) {
            SharedTransitionLayout(
                modifier = Modifier.fillMaxSize()
            ) {
                NavHost(
                    navController = navController,
                    startDestination = HOME_SCREEN_ROUTE
                ) {
                    homeScreenNavGraph(
                        onDetailPressed = { movieId ->
                            navController.navigateToDetailScreen(movieId)
                        }
                    )
                    detailScreenNavGraph(
                        onBackPressed = navController::popBackStack,
                        sharedTransitionScope = this@SharedTransitionLayout
                    )
                    searchScreenNavGraph(
                        onDetailPressed = { movieId ->
                            navController.navigateToDetailScreen(movieId)
                        }
                    )
                    savedScreenGraph(
                        onDetail = {
                            navController.navigateToDetailScreen(it)
                        },
                        sharedTransitionScope = this@SharedTransitionLayout
                    )
                }
            }
        }
    }
}

val LocalPaddings = compositionLocalOf { PaddingValues(0.dp) }

@OptIn(ExperimentalMaterial3Api::class)
val LocalTopAppBarState = compositionLocalOf { TopAppBarState(0f, 0f, 0f) }