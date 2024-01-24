package com.example.instagramclone.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.instagramclone.presentation.edit_email.EditEmailScreen
import com.example.instagramclone.presentation.edit_password.EditPasswordScreen
import com.example.instagramclone.presentation.profile.ProfileScreen
import com.example.instagramclone.presentation.users_list.UsersListNavigation
import com.example.instagramclone.presentation.users_list.UsersListScreen


fun NavGraphBuilder.ProfileNavGraph(navController: NavHostController) {

    navigation(
        route = Graphs.PROFILE + "/{userId}",
        startDestination = Screens.ProfileScreen.route,
        arguments = listOf(navArgument("userId") {
            type = NavType.StringType
        })
    ) {
        composable(
            route = Screens.ProfileScreen.route
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ProfileScreen(navController = navController,userId)
        }
        composable(Screens.EditEmailScreen.route) {
            EditEmailScreen()
        }
        composable(Screens.EditPasswordScreen.route) {
            EditPasswordScreen()
        }

        composable(Screens.UsersListScreen.route + "/{id}/{navigation_type}",
            arguments = listOf(navArgument("navigation_type") {
                type = NavType.EnumType(UsersListNavigation::class.java)
            }, navArgument("id") {
                type = NavType.StringType
            })
        ) { backStackEntry ->

            val navigationType = (backStackEntry.arguments?.getString("navigation_type")
                ?: UsersListNavigation.FOLLOWING) as UsersListNavigation
            val userId = backStackEntry.arguments?.getString("id") ?: ""
            UsersListScreen(
                navController = navController,
                id = userId,
                navigationType = navigationType
            )
        }
    }
}
