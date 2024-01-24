package com.example.instagramclone.presentation.navigation

sealed class Screens(val route: String) {
    object SplashScreen: Screens("splash_screen")
    object LoginScreen: Screens("login_screen")
    object SignUpScreen: Screens("signup_screen")
    object FeedsScreen: Screens("feed_screen")
    object SearchScreen: Screens("search_screen")
    object ProfileScreen: Screens("profile_screen")
    object ChatDetailsScreen: Screens("chat_details_screen")
    object ChatListsScreen: Screens("chat_list_screen")
    object NotificationsScreen: Screens("notifications_screen")
    object EditEmailScreen: Screens("edit_email_screen")

    object EditPasswordScreen: Screens("edit_password_screen")
    object AddPostScreen: Screens("add_post_screen")
    object AddStoryScreen: Screens("add_story_screen")
    object StoryDetailsScreen: Screens("story_details_screen")
    object PostDetailsScreen: Screens("post_details_screen")


    object ImageViewScreen: Screens("image_view_screen")
    object AddNewFollowingScreen:Screens("add_new_following_screen")
    object UsersListScreen:Screens("users_list_screen")


}
