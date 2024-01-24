package com.example.instagramclone.presentation.post_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.instagramclone.presentation.feed.componrnts.PostItem
import com.example.instagramclone.presentation.feed.componrnts.PostNavigationType
import com.example.instagramclone.presentation.navigation.Screens
import com.example.instagramclone.presentation.post_details.components.CommentBox
import com.example.instagramclone.presentation.post_details.components.CommentItem


@Composable
fun PostDetails(
    navController: NavHostController,
    postId: String,
    navigationType: PostDetailsNavigationType
) {

    val viewModel: PostViewModel = hiltViewModel()

    LaunchedEffect(key1 = true) {
        postId.let {
            viewModel.getPost(postId)
            viewModel.getPostComments(postId)
        }
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (viewModel.showCircularProgressIndicator.value)
            BallZigZagProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                )
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.Top
            ) {
                viewModel.postData.value.data?.let { post ->
                    item {
                        PostItem(
                            post,
                            viewModel.userId.value,
                            {
                                if (!post.likes.contains(viewModel.userId.value))
                                    viewModel.addLike(
                                        post.postId,
                                        (post.userId!= viewModel.userId.value),
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
                            },
                            {
                                viewModel.deletePost(post.postId, viewModel.userId.value)
                                navController.popBackStack()
                                if (navigationType == PostDetailsNavigationType.FROM_FEED)
                                    navController.navigate(Screens.FeedsScreen.route)
                                else if(navigationType == PostDetailsNavigationType.FROM_PROFILE)
                                    navController.navigate(Screens.ProfileScreen.route)

                            },
                            navController,
                            PostNavigationType.IMAGE_VIEWER_SCREEN,
                            PostDetailsNavigationType.FROM_FEED
                        )
                    }

                    viewModel.commentsData.value.data?.let { comments ->
                        items(comments) { comment ->
                            CommentItem(viewModel.userId.value, comment) { commentId ->
                                viewModel.deleteComment(post.postId, commentId)
                            }
                        }
                    }
                }

                item {
                    CommentBox(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {comment ->
                    viewModel.postData.value.data?.let{
                            viewModel.postData.value.data?.let {
                                viewModel.addComment(
                                    postId,
                                    (it.userId!= viewModel.userId.value),
                                    comment,
                                    viewModel.userId.value,
                                    viewModel.userName.value,
                                    it.notificationToken,
                                    it.postDescription,
                                    it.userId
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

