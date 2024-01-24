package com.example.instagramclone.presentation.navigation

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.intuit.sdp.R


data class BottomNavigationItem(val icon: ImageVector, val route: Screens)

@SuppressLint("RestrictedApi")
@Composable
fun BottomNavigationMenu(
    navController: NavController
) {
    var selectedIndex by remember {
        mutableIntStateOf(0)

    }
    var backPressed by remember {
        mutableStateOf(false)
    }

    val list = listOf(
        BottomNavigationItem(
            if (selectedIndex == 0)
                Icons.Default.Home
            else
                Icons.Outlined.Home, Screens.FeedsScreen
        ),
        BottomNavigationItem(

            if (selectedIndex == 1)
                Icons.Default.Search
            else
                Icons.Outlined.Search, Screens.SearchScreen
        ),
        BottomNavigationItem(

            if (selectedIndex == 2)
                Icons.Default.Add
            else
                Icons.Outlined.Add, Screens.ChatListsScreen
        ),


        BottomNavigationItem(

            if (selectedIndex == 3)
                Icons.Default.ChatBubble
            else
                Icons.Outlined.ChatBubbleOutline, Screens.ChatListsScreen
        ),

        BottomNavigationItem(

            if (selectedIndex == 4)
                Icons.Default.Notifications
            else
                Icons.Outlined.Notifications, Screens.NotificationsScreen
        ),
    )
    val screens = listOf(
        Screens.NotificationsScreen.route,
        Screens.FeedsScreen.route,
        Screens.SearchScreen.route,
        Screens.ChatListsScreen.route,
        Screens.AddPostScreen.route
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it == currentDestination?.route }

    if (backPressed) {
        if (selectedIndex != 0) {
            navController.popBackStack()
            selectedIndex = 0
            navController.navigate(Screens.FeedsScreen.route)
        } else {
            val activity = (LocalContext.current as? Activity)
            activity?.finish()
        }
        backPressed = false
    }
    BackHandler {
        backPressed = true
    }


    if (bottomBarDestination) {

        Divider(thickness = dimensionResource(id = com.intuit.sdp.R.dimen._1sdp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen._45sdp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            list.forEachIndexed { index, item ->
                Icon(
                    imageVector = item.icon,
                    contentDescription = "",
                    modifier = Modifier
                        .size(dimensionResource(id = com.intuit.sdp.R.dimen._30sdp))
                        .weight(1f)
                        .padding(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
                        .clickable {
                            selectedIndex = index
                            navController.popBackStack()
                            when (selectedIndex) {
                                0 -> {
                                    navController.navigate(Screens.FeedsScreen.route)
                                }
                                1 -> {
                                    navController.navigate(Screens.SearchScreen.route)
                                }

                                2 -> {
                                    navController.navigate(Screens.AddPostScreen.route)
                                }

                                3 -> {
                                    navController.navigate(Graphs.CHAT)
                                }

                                else -> {
                                    navController.navigate(Screens.NotificationsScreen.route)
                                }
                            }
                        }
                )
            }
        }
    }
}



