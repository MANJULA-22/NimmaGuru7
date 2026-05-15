package com.example.nimmaguru.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object WallOfFame : Screen("wall_of_fame")
    object Calendar : Screen("calendar")
    object PostReview : Screen("post_review/{guruId}/{guruName}") {
        fun createRoute(guruId: String, guruName: String) = "post_review/$guruId/$guruName"
    }
}