package com.example.mobileclient.bottomnav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mobileclient.R
import com.example.mobileclient.navigation.routes.Screens
import com.example.mobileclient.ui.theme.Purple40
import com.example.mobileclient.ui.theme.Purple80

@Composable
fun BottomNavMenu(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        TopLevelRoute("Тексты", Screens.TextList.route, Icons.Default.Home),
        TopLevelRoute(
            "История произношений", Screens.FeedbackHistory.route, ImageVector.vectorResource(
                R.drawable.history_icon
            )
        )
    )
    val routes = items.map { it.route }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    if ((currentDestination != null) && (routes.contains(currentDestination.route))
    ) {
        NavigationBar(
            modifier = modifier,
            containerColor = Purple40
        ) {
            items.forEach { screen ->
                val isSelected =
                    currentDestination?.hierarchy?.any { it.route == screen.route } == true
                NavigationBarItem(
                    selected = isSelected,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Purple80
                    ),
                    icon = {
                        Icon(
                            painter = rememberVectorPainter(
                                image = screen.icon
                            ),
                            tint = Color.Black,
                            contentDescription = "${screen.route} icon"
                        )
                    },
                    label = {
                        Text(
                            text = screen.name,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            softWrap = false
                        )
                    },
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}