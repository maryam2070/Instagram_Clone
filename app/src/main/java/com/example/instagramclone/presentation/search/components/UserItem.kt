package com.example.instagramclone.presentation.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.instagramclone.domain.model.User
import com.example.instagramclone.presentation.navigation.Graphs
import com.example.instagramclone.utils.getImageLink
import com.intuit.sdp.R

@Composable
fun UserItem(navController: NavController, user: User) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen._10sdp))
            .clickable {
                navController.navigate(Graphs.PROFILE + "/" + user.id)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = getImageLink(user.id),
            contentDescription = null,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen._40sdp))
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(
            text = user.name,
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen._10sdp)),
            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
        )

    }
}
