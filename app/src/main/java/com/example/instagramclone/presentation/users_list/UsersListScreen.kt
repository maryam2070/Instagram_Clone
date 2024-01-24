package com.example.instagramclone.presentation.users_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ehsanmsz.mszprogressindicator.progressindicator.BallZigZagProgressIndicator
import com.example.instagramclone.presentation.users_list.components.Header
import com.example.instagramclone.presentation.users_list.components.UserItem

@Composable
fun UsersListScreen(
    navController: NavHostController,
    id: String,
    navigationType: UsersListNavigation
) {

    val viewModel: UsersListViewModel = hiltViewModel()

    LaunchedEffect(key1 = true, block = {
        if (navigationType == UsersListNavigation.FOLLOWING)
            viewModel.getFollowing(id)
        else if (navigationType == UsersListNavigation.FOLLOWERS)
            viewModel.getFollowers(id)
        else {
            viewModel.getLikes(id)
        }
    })

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (viewModel.uiState.value.isLoading)
            BallZigZagProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
            )

        LazyColumn(modifier = Modifier
            .fillMaxSize(), content = {

            item {
                Header(navigationType, navController)
            }
            items(viewModel.uiState.value.users) { user ->
                UserItem(user, navController)
            }
        })
    }
}

