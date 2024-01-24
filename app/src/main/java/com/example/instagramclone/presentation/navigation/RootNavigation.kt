package com.example.instagramclone.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun RootNavigation(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Graphs.AUTHENTICATION
    ) {
            AuthNavGraph(navController = navController)
            PagesNavGraph(navController = navController)
            ChatNavGraph(navController = navController)
            ProfileNavGraph(navController = navController)
            PostNavGraph(navController)

    }
}
