package com.example.nimmaguru.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.nimmaguru.ui.screens.calendar.CalendarScreen
import com.example.nimmaguru.ui.screens.home.HomeScreen
import com.example.nimmaguru.ui.screens.login.LoginScreen
import com.example.nimmaguru.ui.screens.profile.GuruProfileScreen
import com.example.nimmaguru.ui.screens.review.PostReviewScreen
import com.example.nimmaguru.ui.screens.wall.WallOfFameScreen

@Composable
fun NimmaGuruNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { isMentor ->
                    if (isMentor) {
                        navController.navigate(Screen.Profile.route)
                    } else {
                        navController.navigate(Screen.Home.route)
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onGuruClick = { id, name ->
                    navController.navigate(Screen.PostReview.createRoute(id, name))
                },
                onWallOfFameClick = {
                    navController.navigate(Screen.WallOfFame.route)
                },
                onCalendarClick = {
                    navController.navigate(Screen.Calendar.route)
                }
            )
        }
        composable(Screen.Profile.route) {
            GuruProfileScreen(onProfileSaved = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Profile.route) { inclusive = true }
                }
            })
        }
        composable(Screen.WallOfFame.route) {
            WallOfFameScreen(
                onGuruClick = { id ->
                    // Logic for Guru Detail or Review
                }
            )
        }
        composable(Screen.Calendar.route) {
            CalendarScreen()
        }
        composable(
            route = Screen.PostReview.route,
            arguments = listOf(
                navArgument("guruId") { type = NavType.StringType },
                navArgument("guruName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val guruId = backStackEntry.arguments?.getString("guruId") ?: ""
            val guruName = backStackEntry.arguments?.getString("guruName") ?: ""
            PostReviewScreen(
                guruId = guruId,
                guruName = guruName,
                onBack = { navController.popBackStack() }
            )
        }
    }
}