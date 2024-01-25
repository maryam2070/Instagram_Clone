package com.example.instagramclone.presentation.chat.chat_list.components

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
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.instagramclone.domain.model.ChatForUser
import com.example.instagramclone.presentation.navigation.Screens
import com.example.instagramclone.utils.getImageLink
import com.example.instagramclone.utils.getTimeFormat
import com.google.gson.Gson
import com.intuit.sdp.R

@Composable
fun UserItem(navController: NavController, item: ChatForUser) {

    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen._5sdp))
            .clickable {

                navController.navigate(Screens.ChatDetailsScreen.route + "/${Gson().toJson(item)}") {

                }
            }
    ) {
        val (img, colName, time) = createRefs()
        AsyncImage(
            model = getImageLink(item.imgUrl),
            contentDescription = null,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen._45sdp))
                .clip(CircleShape)
                .constrainAs(img) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                },

            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.constrainAs(colName) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(img.end)
        }) {
            Text(text = item.friendName,
                Modifier.padding(dimensionResource(id = R.dimen._5sdp)))
            Text(text = item.lastMessage,
                Modifier.padding(dimensionResource(id = R.dimen._3sdp)),
                color = Color.Gray)
        }
        Text(
            text = getTimeFormat(item.lastMessageTime.toDate()),
            Modifier
                .padding(end = dimensionResource(id = R.dimen._10sdp))
                .constrainAs(time) {
                    top.linkTo(colName.top)
                    bottom.linkTo(colName.bottom)
                    end.linkTo(parent.end)
                }, fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp, color = Color.Gray
        )
    }
}
