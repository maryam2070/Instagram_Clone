package com.example.instagramclone.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.instagramclone.domain.model.ChatForUser
import com.example.instagramclone.presentation.chat.chat_details.ChatRoomScreen
import com.example.instagramclone.presentation.chat.chat_list.ChatListScreen
import com.google.gson.Gson


fun NavGraphBuilder.ChatNavGraph(navController: NavHostController) {


    val uri = "app://example.instagramclone.com"

    navigation(
        route = Graphs.CHAT,
        startDestination = Screens.ChatListsScreen.route,
    ) {

        composable(
            route = Screens.ChatListsScreen.route
        ) { _ ->
            ChatListScreen(
                navController = navController
            )

        }

        composable(
            route = Screens.ChatDetailsScreen.route + "/{chatItem}",
            arguments = listOf(navArgument("chatItem") {
                type = NavType.StringType
            }),
            deepLinks = listOf(navDeepLink { uriPattern = "$uri/chatItem={chatItem}" })
        ) { backStackEntry ->
            val chatItem = backStackEntry.arguments?.getString("chatItem")


            ChatRoomScreen(
                chatItem = Gson().fromJson(chatItem, ChatForUser::class.java)
            )
        }
    }
}

