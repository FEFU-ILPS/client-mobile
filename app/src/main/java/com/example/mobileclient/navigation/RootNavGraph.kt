package com.example.mobileclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.mobileclient.navigation.routes.Graphs


@Composable
fun RootNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Graphs.Auth.route
    ) {
        authNavGraph(navController)
        textsNavGraph(navController)
    }
}