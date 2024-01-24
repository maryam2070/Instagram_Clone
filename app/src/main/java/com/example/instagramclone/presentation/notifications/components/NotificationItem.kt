package com.example.instagramclone.presentation.notifications.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.instagramclone.domain.model.Notification
import com.example.instagramclone.presentation.navigation.Graphs
import com.example.instagramclone.presentation.post_details.PostDetailsNavigationType
import com.example.instagramclone.utils.getImageLink
import com.intuit.sdp.R


@Composable
fun NotificationItem(navController:NavController,item: Notification) {
    ConstraintLayout(Modifier.fillMaxWidth().
    padding(dimensionResource(id = R.dimen._5sdp))
        .clickable {
            navController.navigate(Graphs.POST+"/${item.postId}/${PostDetailsNavigationType.NONE}")
        }) {
        val (img,colName)=createRefs()
        AsyncImage(
            model = getImageLink(item.authorId),
            modifier = Modifier
                .size(dimensionResource(id = R.dimen._50sdp))
                .clip(CircleShape)
                .constrainAs(img) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                },
            contentScale = ContentScale.Crop,
            contentDescription = "",
        )

        Column(modifier = Modifier.constrainAs(colName){
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(img.end)
        }){
            Text(text = item.title, Modifier.padding(dimensionResource(id = R.dimen._3sdp)))
            Text(text = item.body, Modifier.padding(dimensionResource(id = R.dimen._3sdp)), color = Color.Gray)
        }

    }
}
