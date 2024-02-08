package com.akz.cinema

import androidx.compose.foundation.interaction.MutableInteractionSource
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
import com.akz.cinema.ui.components.BackPressInteraction
import com.akz.cinema.ui.components.CinemaTopAppBar
import com.akz.cinema.ui.components.ConnectivityStatusBar
import com.akz.cinema.ui.screen.detail.detailScreenNavGraph
import com.akz.cinema.ui.screen.detail.navigateToDetailScreen
import com.akz.cinema.ui.screen.home.HOME_SCREEN_ROUTE
import com.akz.cinema.ui.screen.home.homeScreenNavGraph
import com.akz.cinema.ui.screen.search.navigateToSearchScreen
import com.akz.cinema.ui.screen.search.searchScreenNavGraph
import kotlinx.coroutines.isActive

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    viewModel: MainActivityViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val interactionSource = remember { MutableInteractionSource() }
    var screen by remember { mutableStateOf(Screen.Home) }
    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()
    val topAppBarState = rememberTopAppBarState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topAppBarState,
        canScroll = {
            screen.topAppBarScrollable
        }
    )

    LaunchedEffect(backStackEntry) {
        screen = backStackEntry?.destination?.getScreen() ?: Screen.Home
        topAppBarState.heightOffset = 0f
        topAppBarState.contentOffset = 0f
    }

    LaunchedEffect(Unit) {
        while (isActive) {
            interactionSource.interactions.collect {
                when (it) {
                    is BackPressInteraction -> navController.popBackStack()
                }
            }
        }
    }



    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = {
            ConnectivityStatusBar(
                isConnected = isConnected
            )
        },
        topBar = {
            if (screen.hasTopBar) {
                CinemaTopAppBar(
                    screen = screen,
                    interactionSource = interactionSource,
                    scrollBehavior = scrollBehavior
                )
            }
        }
    ) { paddings ->
        CompositionLocalProvider(
            LocalPaddings provides paddings,
            LocalTopAppBarState provides topAppBarState
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
                    interactionSource = interactionSource
                )

                detailScreenNavGraph(
                    onBackPressed = navController::popBackStack
                )
                searchScreenNavGraph(
                    onDetailPressed = { movieId ->
                        navController.navigateToDetailScreen(movieId)
                    }
                )
            }
        }
    }
}

val LocalPaddings = compositionLocalOf { PaddingValues(0.dp) }

@OptIn(ExperimentalMaterial3Api::class)
val LocalTopAppBarState = compositionLocalOf { TopAppBarState(0f, 0f, 0f) }