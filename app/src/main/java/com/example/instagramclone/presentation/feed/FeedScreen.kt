package com.example.instagramclone.presentation.feed

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ehsanmsz.mszprogressindicator.progressindicator.BallZigZagProgressIndicator
import com.example.instagramclone.R
import com.example.instagramclone.presentation.feed.componrnts.PostItem
import com.example.instagramclone.presentation.feed.componrnts.PostNavigationType
import com.example.instagramclone.presentation.feed.componrnts.StoryView
import com.example.instagramclone.presentation.navigation.Graphs
import com.example.instagramclone.presentation.navigation.Screens
import com.example.instagramclone.presentation.post_details.PostDetailsNavigationType
import com.example.instagramclone.utils.getImageLink
import com.google.gson.Gson


@SuppressLint("CoroutineCreationDuringComposition", "SuspiciousIndentation")
@Composable
fun FeedsScreen(navController: NavController) {
    val viewModel: FeedsViewModel = hiltViewModel()


    CheckNotificationPermission()

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        if (viewModel.uiState.value.isLoading) {
            BallZigZagProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
            )
         } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    ProfileIconRow(viewModel.userId.value, navController)
                }
                item {
                    StoriesRow(viewModel, navController)
                }

                if (viewModel.uiState.value.feed.isEmpty()) {

                    item {
                        EmptyFeedView(navController)
                    }
                } else {
                    items(viewModel.uiState.value.feed) { post ->

                        PostItem(post, viewModel.userId.value, {
                            if (!post.likes.contains(viewModel.userId.value))
                                viewModel.addLike(
                                post.postId,
                                (post.userId!= viewModel.userId.value),
                                viewModel.userId.value, viewModel.userName.value,
                                post.notificationToken,
                                post.postDescription,
                                post.userId
                            )
                            else viewModel.deleteLike(
                                viewModel.userId.value,
                                post.postId,
                                likeId = viewModel.userId.value
                            )
                        }, {
                            viewModel.deletePost(post.postId, viewModel.userId.value)
                        }, navController, PostNavigationType.POST_DETAILS_SCREEN,PostDetailsNavigationType.FROM_FEED
                        )
                    }
                }
            }
        }

    }
}

@Composable
private fun EmptyFeedView(navController: NavController) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.empty_data_feed),
            contentDescription = "",
            modifier = Modifier
        )
        OutlinedButton(
            onClick = {
                navController.navigate(Screens.AddNewFollowingScreen.route)
            }, modifier = Modifier
                .wrapContentWidth()
                .padding(
                    start = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp),
                    end = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp),
                    bottom = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
                )
                .clip(RoundedCornerShape(20.dp))

        ) {
            Text("Discover New Friends", Modifier.padding(5.dp))
        }
    }
}

@Composable
private fun StoriesRow(
    viewModel: FeedsViewModel,
    navController: NavController
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(dimensionResource(id = com.intuit.sdp.R.dimen._8sdp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item() {
            YourStory(imageUrl = viewModel.userId.value, navController)
        }

        items(viewModel.uiState.value.stories) { story ->
            StoryView(story) {
                viewModel.addStoryToDataStore(story.id, true)
                val json = Uri.encode(Gson().toJson(story))
                navController.navigate(Screens.StoryDetailsScreen.route + "/" + json + "/" + viewModel.userId.value)
            }
        }
    }
}

@Composable
private fun ProfileIconRow(
    userId: String,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = getImageLink(userId),
            contentDescription = "",
            modifier = Modifier
                .clip(CircleShape)
                .size(dimensionResource(id = com.intuit.sdp.R.dimen._30sdp))
                .clickable {
                    navController.navigate(Graphs.PROFILE + "/" + userId)
                },
            contentScale = Crop
        )

    }
}

@Composable
private fun CheckNotificationPermission() {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {

    }

    LaunchedEffect(key1 = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }
}

@Composable
fun YourStory(imageUrl: String, navController: NavController) {

    Box(modifier = Modifier
        .padding(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp))
        .size(dimensionResource(id = com.intuit.sdp.R.dimen._75sdp))
        .clip(RectangleShape)
        .clip(RoundedCornerShape(20.dp))
        .clickable {
            navController.navigate(Screens.AddStoryScreen.route)
        }) {
        AsyncImage(
            model = getImageLink(imageUrl), contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Icon(
            Icons.Default.AddCircle,
            contentDescription = null,
            modifier = Modifier.align(Alignment.BottomEnd),
            tint = Color(0xFF2196F3)
        )
    }
}
