package com.example.instagramclone.presentation.discover.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.instagramclone.domain.model.User
import com.example.instagramclone.presentation.navigation.Graphs
import com.example.instagramclone.utils.getImageLink
import com.intuit.sdp.R


@Composable
fun FollowingItem(navController: NavController, user: User, onBtnClick: () -> Unit) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(
                vertical = dimensionResource(id = R.dimen._5sdp),
                horizontal = dimensionResource(id = R.dimen._10sdp)
            )
            .clickable {
                navController.navigate(Graphs.PROFILE + "/" + user.id)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            AsyncImage(
                model = getImageLink(user.id),
                contentDescription = null,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen._50sdp))
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = user.name,
                    Modifier.padding(
                        bottom = dimensionResource(id = R.dimen._5sdp),
                        start = dimensionResource(id = R.dimen._7sdp)
                    )
                )
                Text(
                    text = user.bio,
                    Modifier.padding(
                        bottom = dimensionResource(id = R.dimen._5sdp),
                        start = dimensionResource(id = R.dimen._7sdp)
                    )
                )
            }
        }
        OutlinedButton(onClick = {
            onBtnClick()
        }, modifier = Modifier.align(Alignment.Top)) {
            Text(text = "Follow")
        }
    }

}
