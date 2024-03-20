package com.akz.cinema.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.akz.cinema.lib.NavigationItem
import com.akz.cinema.ui.screen.search.isSearchScreenCurrentDestination
import kotlinx.coroutines.launch

@Composable
fun CinemaNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val navItems = NavigationItem.entries.toList()
    var selected by remember {
        mutableIntStateOf(0)
    }
    val backStackEntry by navController.currentBackStackEntryAsState()
    LaunchedEffect(backStackEntry) {
        selected = if (backStackEntry?.destination?.isSearchScreenCurrentDestination() == true) {
            1
        } else {
            0
        }
    }
    NavigationBar(
        modifier = modifier
    ) {
        navItems.forEachIndexed { index, item ->
            val anim = remember {
                Animatable(100, Int.VectorConverter)
            }
            val anim2 = remember {
                Animatable(0f, Float.VectorConverter)
            }
            LaunchedEffect(Unit) {
                launch {
                    anim.animateTo(
                        0,
                        animationSpec = tween(
                            300,
                            delayMillis = 200 + (index * 100)
                        )
                    )
                }
                launch {
                    anim2.animateTo(
                        1f,
                        animationSpec = tween(
                            300,
                            delayMillis = 200 + (index * 100)
                        )
                    )
                }
            }
            NavigationBarItem(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = 0,
                            y = anim.value
                        )
                    }
                    .graphicsLayer {
                        alpha = anim2.value
                    },
                selected = selected == index,
                onClick = {
                    selected = index
                    with(item) {
                        navController.navigateTo(navOptions)
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected == index) item.selectedImageVector else item.unSelectedImageVector,
                        contentDescription = item.title
                    )
                }
            )
        }
    }
}