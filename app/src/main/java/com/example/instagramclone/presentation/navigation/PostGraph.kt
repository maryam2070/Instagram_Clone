package com.example.instagramclone.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.instagramclone.presentation.image_view.ImageViewScreen
import com.example.instagramclone.presentation.post_details.PostDetails
import com.example.instagramclone.presentation.post_details.PostDetailsNavigationType

fun NavGraphBuilder.PostNavGraph(navController: NavHostController) {


    val uri = "app://example.instagramclone.com"

    navigation(
        route = Graphs.POST + "/{postId}/{navigation_type}",
        startDestination = Screens.PostDetailsScreen.route,
        arguments = listOf(
            navArgument("postId") { type = NavType.StringType },
        navArgument("navigation_type") {
            type = NavType.EnumType(PostDetailsNavigationType::class.java)
        })
    ) {


        composable(route = Screens.PostDetailsScreen.route,
            deepLinks = listOf(navDeepLink { uriPattern = "$uri/postId={postId}" })) { backStackEntry ->

            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            val navigationType = (backStackEntry.arguments?.getString("navigation_type") ?: PostDetailsNavigationType.FROM_FEED) as PostDetailsNavigationType

            PostDetails(navController, postId = postId,navigationType)
        }

        composable(route = Screens.ImageViewScreen.route + "/{imagePath}",
            arguments = listOf(navArgument("imagePath") {
                type = NavType.StringType
            })) { backStackEntry ->
            ImageViewScreen(backStackEntry.arguments?.getString("imagePath"))
        }

    }
}