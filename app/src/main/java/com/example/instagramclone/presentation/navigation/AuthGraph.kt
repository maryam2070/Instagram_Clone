package com.example.instagramclone.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.instagramclone.presentation.login.LoginScreen
import com.example.instagramclone.presentation.signup.SignUpScreen
import com.example.instagramclone.presentation.splash.SplashScreen

fun NavGraphBuilder.AuthNavGraph(
    navController: NavHostController
) {

    navigation(
        route = Graphs.AUTHENTICATION,
        startDestination = Screens.SplashScreen.route,
        ) {
        composable(Screens.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
        composable(Screens.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(Screens.SignUpScreen.route) {
            SignUpScreen(navController = navController)
        }
    }
}