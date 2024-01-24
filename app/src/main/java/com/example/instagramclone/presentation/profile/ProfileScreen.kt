package com.example.instagramclone.presentation.profile

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ehsanmsz.mszprogressindicator.progressindicator.BallZigZagProgressIndicator
import com.example.instagramclone.R
import com.example.instagramclone.domain.model.Friend
import com.example.instagramclone.domain.model.ProfileNavigationType
import com.example.instagramclone.domain.model.User
import com.example.instagramclone.presentation.feed.componrnts.PostItem
import com.example.instagramclone.presentation.feed.componrnts.PostNavigationType
import com.example.instagramclone.presentation.navigation.Graphs
import com.example.instagramclone.presentation.navigation.Screens
import com.example.instagramclone.presentation.post_details.PostDetailsNavigationType
import com.example.instagramclone.presentation.profile.components.EditDialog
import com.example.instagramclone.presentation.profile.components.FriendProfileView
import com.example.instagramclone.presentation.profile.components.MyProfile
import com.example.instagramclone.presentation.profile.components.NotFriendProfileView
import com.example.instagramclone.presentation.profile.components.ProfileStates
import com.example.instagramclone.presentation.users_list.UsersListNavigation
import com.example.instagramclone.utils.Constants
import com.example.instagramclone.utils.getImageLink
import com.intuit.sdp.R.dimen

@SuppressLint("SuspiciousIndentation")
@Composable
fun ProfileScreen(
    navController: NavController,
    userId: String
) {
    val viewModel: UserViewModel = hiltViewModel()

    LaunchedEffect(key1 = true, block = {
        viewModel.checkIfFollowerExist(viewModel.userId.value, userId)

        viewModel.getUserInfo(userId)
        viewModel.getPosts(userId)
    })

    if (viewModel.uiState.value.isError) {
        val context = LocalContext.current
        LaunchedEffect(key1 = true, block = {
            Toast.makeText(
                context,
                "There Is Error " + viewModel.uiState.value.errorMessage,
                Toast.LENGTH_LONG
            ).show()
        })
    }

    if (viewModel.uiState.value.signedOut) {
        navController.navigate(Graphs.AUTHENTICATION)
    }

    Box(contentAlignment = Alignment.Center) {

        if (viewModel.uiState.value.isLoading) {
            BallZigZagProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
            )
        }
        viewModel.uiState.value.user.let { user ->

            if (viewModel.showEditDialog.value) {
                EditDialog(user, { userId, map ->
                    viewModel.updateUserData(userId, map)
                }, {
                    viewModel.setShowEditDialog(false)
                }, { uri ->
                    viewModel.updateUserProfilePhoto(uri)
                })
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                item {
                    ImageNameBioView(user, userId, viewModel, navController)
                }

                item {
                    MyProfile(
                        displayName = user.name, bio = user.bio
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    CheckNavigationType(viewModel, user, navController)
                    Spacer(modifier = Modifier.height(dimensionResource(id = dimen._15sdp)))
                }

                items(viewModel.uiState.value.posts) { post ->
                    PostItem(post, viewModel.userId.value, {
                        if (!post.likes.contains(viewModel.userId.value))
                            viewModel.addLike(
                                post.postId,
                                (post.userId != viewModel.userId.value),
                                viewModel.userId.value,
                                viewModel.userName.value,
                                post.notificationToken,
                                post.postDescription,
                                post.userId
                            )
                        else
                            viewModel.deleteLike(
                                viewModel.userId.value,
                                post.postId,
                                likeId = viewModel.userId.value
                            )
                    }, {
                        viewModel.deletePost(post.postId, viewModel.userId.value)
                        navController.popBackStack()
                        navController.navigate(Screens.ProfileScreen.route)
                    }, navController,
                        PostNavigationType.POST_DETAILS_SCREEN,
                        PostDetailsNavigationType.FROM_PROFILE
                    )
                }

            }
        }
    }
}

@Composable
private fun ImageNameBioView(
    user: User,
    userId: String,
    viewModel: UserViewModel,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(id = dimen._10sdp),
                end = dimensionResource(id = dimen._20sdp),
                top = dimensionResource(id = dimen._10sdp),
                bottom = dimensionResource(id = dimen._10sdp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        AsyncImage(
            model = getImageLink(user.id),
            contentDescription = null,
            modifier = Modifier
                .size(dimensionResource(id = dimen._60sdp))
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Column {

            if (userId == viewModel.userId.value) {
                SignOutRow(viewModel)
            }
            InfoView(user, navController)
        }
    }
}

@Composable
private fun InfoView(
    user: User,
    navController: NavController
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        ProfileStates(
            numberText = user.totalPosts.toString(),
            text = "Posts"
        ) {}
        ProfileStates(
            numberText = user.followers.toString(),
            text = "Followers"
        ) {
            navController.navigate(Screens.UsersListScreen.route + "/${user.id}/${UsersListNavigation.FOLLOWERS}")
        }
        ProfileStates(
            numberText = user.following.toString(),
            text = "Following"
        ) {
            navController.navigate(Screens.UsersListScreen.route + "/${user.id}/${UsersListNavigation.FOLLOWING}")
        }

    }
}

@Composable
private fun CheckNavigationType(
    viewModel: UserViewModel,
    user: User,
    navController: NavController
) {
    when (viewModel.uiState.value.navigationType) {
        ProfileNavigationType.USER_PROFILE -> {

            UserProfileView { viewModel.setShowEditDialog(true) }
        }

        ProfileNavigationType.NOT_FRIEND_PROFILE -> {
            NotFriendProfileView(viewModel.userId.value, user, navController) {
                viewModel.addFollower(
                    viewModel.userId.value,
                    viewModel.userName.value,
                    Friend(user.name, user.id)
                )
            }
        }

        ProfileNavigationType.FRIEND_PROFILE -> {
            FriendProfileView(
                viewModel.userId.value,
                viewModel.uiState.value.user,
                navController,
            ) {
                viewModel.removeFollower(
                    viewModel.userId.value,
                    Friend(user.name, user.id)
                )
            }
        }
    }
}

@Composable
private fun UserProfileView(onClick: () -> Unit) {
    OutlinedButton(
        onClick = {
            onClick()

        }, modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(id = dimen._20sdp),
                end = dimensionResource(id = dimen._20sdp),
                bottom = dimensionResource(id = dimen._20sdp)
            )
            .clip(RoundedCornerShape(dimensionResource(id = dimen._20sdp)))
    ) {
        Text(
            "Edit Profile",
            Modifier.padding(dimensionResource(id = dimen._5sdp))
        )
    }
}

@Composable
private fun SignOutRow(viewModel: UserViewModel) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            painterResource(R.drawable.sign_out_ic),
            contentDescription = "",
            modifier = Modifier
                .size(
                    dimensionResource(id = dimen._16sdp)
                )
                .clickable {
                    viewModel.signOut()
                },
        )
    }
}



