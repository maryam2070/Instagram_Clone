package com.example.instagramclone.presentation.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.instagramclone.domain.model.ChatForUser
import com.example.instagramclone.domain.model.User
import com.example.instagramclone.presentation.navigation.Screens
import com.google.gson.Gson
import com.intuit.sdp.R


@Composable
fun NotFriendProfileView(currentUserId:String,user: User, navController:NavController, onFollowButtonClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedButton(
            onClick = {
                onFollowButtonClick()
            }, modifier = Modifier
                .wrapContentWidth()
                .padding(
                    start = dimensionResource(id = R.dimen._20sdp),
                    end = dimensionResource(id = R.dimen._20sdp),
                    bottom = dimensionResource(id = R.dimen._20sdp)
                )
                .clip(RoundedCornerShape(20.dp))

        ) {
            Text("Follow", Modifier.padding(5.dp))
        }
        OutlinedButton(
            onClick = {
                val id =(user.id+(currentUserId)).toCharArray().sorted().joinToString("")
                val json = Gson().toJson(
                    ChatForUser(
                        id = id,
                        friendNotificationToken = user.notificationToken,
                        friendName = user.name,
                        imgUrl = user.imageUrl,
                    )
                )
                navController.navigate(Screens.ChatDetailsScreen.route + "/" + json)
            }, modifier = Modifier
                .wrapContentWidth()
                .padding(
                    start = dimensionResource(id = R.dimen._20sdp),
                    end = dimensionResource(id = R.dimen._20sdp),
                    bottom = dimensionResource(id = R.dimen._20sdp)
                )
                .clip(RoundedCornerShape(20.dp))
            //  colors = ButtonDefaults.buttonColors()

        ) {
            Text(
                "Message",
                Modifier.padding(dimensionResource(id = R.dimen._5sdp))
            )
        }
    }
}
