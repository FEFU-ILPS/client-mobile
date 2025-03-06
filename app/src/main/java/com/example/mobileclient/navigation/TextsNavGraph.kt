package com.example.mobileclient.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.mobileclient.navigation.routes.Graphs
import com.example.mobileclient.navigation.routes.Screens
import com.example.text.TextRoute
import com.example.text_list.TextListRoute

fun NavGraphBuilder.textsNavGraph(navController: NavController) {
    navigation(
        startDestination = Screens.TextList.route,
        route = Graphs.Texts.route
    ) {
        composable(route = Screens.TextList.route) {
            TextListRoute(onSelectText = { id ->
                navController.navigate(Screens.Text.route + "/" + id)
            })
        }

        composable(
            route = Screens.Text.route + "/{textId}",
            listOf(navArgument("textId") { type = NavType.StringType })
        ) {
            TextRoute(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}