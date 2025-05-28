package com.example.mobileclient.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.feedback.FeedbackRoute
import com.example.feedback_history.FeedbackHistoryRoute
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
            route = Screens.Text.route + "/{exerciseId}",
            listOf(navArgument("exerciseId") { type = NavType.StringType })
        ) {
            TextRoute(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onGoToFeedbackScreen = { id ->
                    navController.navigate(Screens.Feedback.route + "/" + id)
                }
            )
        }

        composable(
            route = Screens.Feedback.route + "/{feedbackId}",
            listOf(navArgument("feedbackId") { type = NavType.StringType })
        ) {
            FeedbackRoute(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screens.FeedbackHistory.route
        ) {
            FeedbackHistoryRoute(onGoToFeedback = { id ->
                navController.navigate(Screens.Feedback.route + "/" + id)
            })
        }
    }
}