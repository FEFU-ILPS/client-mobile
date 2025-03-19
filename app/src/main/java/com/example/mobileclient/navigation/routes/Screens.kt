package com.example.mobileclient.navigation.routes


sealed class Screens(
    val route: String
) {
    object TextList : Screens("text_list")

    object Text : Screens("text")

    object Feedback : Screens("feedback")

    object SignUp : Screens("sign_up")

    object SignIn : Screens("sign_in")

    object FeedbackHistory: Screens("feedback_history")
}