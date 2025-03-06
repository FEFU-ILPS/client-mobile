package com.example.mobileclient.navigation.routes


sealed class Screens(
    val route: String
) {
    object TextList : Screens("text_list")

    object Text : Screens("text")
}