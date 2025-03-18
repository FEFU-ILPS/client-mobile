package com.example.mobileclient.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.mobileclient.navigation.routes.Graphs
import com.example.mobileclient.navigation.routes.Screens
import com.example.sign_in.SignInRoute
import com.example.sign_up.SignUpRoute

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation(
        startDestination = Screens.SignIn.route,
        route = Graphs.Auth.route
    ) {
        composable(route = Screens.SignIn.route) {
            SignInRoute(
                onNavigateToTexts = {
                    navController.navigate(Graphs.Texts.route)
                },
                onGoToRegister = {
                    navController.navigate(Screens.SignUp.route)
                }
            )
        }
        composable(route = Screens.SignUp.route) {
            SignUpRoute(
                onGoBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}