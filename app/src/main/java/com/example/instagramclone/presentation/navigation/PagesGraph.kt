package com.example.instagramclone.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.instagramclone.domain.model.Story
import com.example.instagramclone.presentation.add_post.AddPostScreen
import com.example.instagramclone.presentation.discover.AddNewFollowingScreen
import com.example.instagramclone.presentation.feed.FeedsScreen
import com.example.instagramclone.presentation.notifications.NotificationsScreen
import com.example.instagramclone.presentation.search.SearchScreen
import com.example.instagramclone.presentation.story.add_story.AddStoryScreen
import com.example.instagramclone.presentation.story.story_details.StoryDetailsScreen
import com.example.instagramclone.presentation.users_list.UsersListNavigation
import com.example.instagramclone.presentation.users_list.UsersListScreen
import com.google.gson.Gson


fun NavGraphBuilder.PagesNavGraph(navController: NavHostController) {

    navigation(
        route = Graphs.PAGES,
        startDestination = Screens.FeedsScreen.route
    ) {


        composable(
            route = Screens.FeedsScreen.route
        ) { _ ->
            FeedsScreen(
                navController = navController
            )
        }

        composable(
            route = Screens.AddPostScreen.route,
        ) {
            AddPostScreen(navController)
        }

        composable(
            route = Screens.StoryDetailsScreen.route + "/{story}/{userId}",
            arguments = listOf(navArgument("story") {
                type = NavType.StringType
            }, navArgument("userId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->

            val storyJson = backStackEntry.arguments?.getString("story") ?: ""
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            StoryDetailsScreen(story = Gson().fromJson(storyJson, Story::class.java),userId,navController)
        }
        composable(
            route = Screens.AddStoryScreen.route,
        ) {
            AddStoryScreen(navController)
        }

        composable(Screens.NotificationsScreen.route) {
            NotificationsScreen(navController)
        }

        composable(
            route = Screens.SearchScreen.route,
        ) {
            SearchScreen(navController)
        }

        composable(
            route = Screens.AddNewFollowingScreen.route,
        ) {
            AddNewFollowingScreen(navController)
        }

        composable(Screens.UsersListScreen.route+ "/{id}/{navigation_type}",
            arguments = listOf(navArgument("navigation_type") {
                type = NavType.EnumType(UsersListNavigation::class.java)
            }, navArgument("id") {
                type = NavType.StringType
            })) {backStackEntry->

            val navigationType = (backStackEntry.arguments?.getString("navigation_type") ?: UsersListNavigation.FOLLOWING) as UsersListNavigation
            val id = backStackEntry.arguments?.getString("id") ?: ""
            UsersListScreen(navController = navController, id = id, navigationType = navigationType)
        }


    }
}