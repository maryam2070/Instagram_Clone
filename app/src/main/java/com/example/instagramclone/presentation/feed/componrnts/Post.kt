package com.example.instagramclone.presentation.feed.componrnts

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.instagramclone.R
import com.example.instagramclone.domain.model.Post
import com.example.instagramclone.presentation.navigation.Graphs
import com.example.instagramclone.presentation.navigation.Screens
import com.example.instagramclone.presentation.post_details.PostDetailsNavigationType
import com.example.instagramclone.presentation.users_list.UsersListNavigation
import com.example.instagramclone.utils.getImageLink

enum class PostNavigationType{
    POST_DETAILS_SCREEN,
    IMAGE_VIEWER_SCREEN
}
@SuppressLint("SuspiciousIndentation")
@Composable
fun PostItem(
    post: Post,
    userId: String,
    onLikeClick: () -> Unit,
    onDeleteMenuItemClick: () -> Unit,
    navController: NavController,
    navigationType: PostNavigationType,
    postDetailsNavigationType: PostDetailsNavigationType
) {



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                if(navigationType==PostNavigationType.POST_DETAILS_SCREEN){
                    navController.navigate(Graphs.POST+"/${post.postId}/${postDetailsNavigationType}")
                }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = getImageLink(post.userId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(dimensionResource(id = com.intuit.sdp.R.dimen._40sdp))
                        .clip(CircleShape),

                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = post.userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
                )
            }
            if (userId == post.userId) {
                DropDownMenu(onDeleteMenuItemClick)
            }
        }
        Text(
            text = post.postDescription,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp))
        )
        AsyncImage(
                model = getImageLink(post.postId),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = com.intuit.sdp.R.dimen._350sdp))
                .clickable {
                    if(navigationType==PostNavigationType.IMAGE_VIEWER_SCREEN) {
                        navController.navigate(Screens.ImageViewScreen.route+"/"+post.postId)
                    }
                },
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.size(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)))

        Column(Modifier.padding(start = dimensionResource(id =com.intuit.sdp.R.dimen._10sdp))
        ) {
            Text(
                text = "${post.likes.size} people loved that",
                Modifier.clickable {
                    navController.navigate(Screens.UsersListScreen.route + "/${post.postId}/${UsersListNavigation.LIKES}")
                }
            )
        }

        Spacer(modifier = Modifier.size(dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceEvenly
        ) {
            IconButton(modifier = Modifier.size(dimensionResource(id = com.intuit.sdp.R.dimen._30sdp)), onClick = {
                onLikeClick()
            }) {
                if (post.likes.contains(post.userId)) {
                    Icon(
                        painter = painterResource(R.drawable.heart_ic),
                        contentDescription = "",
                        modifier = Modifier.size(dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)),
                        tint = Color.Red
                    )
                }else{
                    Icon(
                        painter = painterResource(R.drawable.outlined_heart_ic),
                        contentDescription = "",
                        modifier = Modifier.size(dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)),
                     )
                }
            }
            IconButton(modifier = Modifier.size(dimensionResource(id = com.intuit.sdp.R.dimen._30sdp)),onClick = {

                navController.navigate(Graphs.POST+"/${post.postId}/${postDetailsNavigationType}")

            }) {
                Icon(
                    painter = painterResource(R.drawable.comment_ic),
                    contentDescription = "",
                    modifier = Modifier.size(dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                )
            }

        }


    }
}

