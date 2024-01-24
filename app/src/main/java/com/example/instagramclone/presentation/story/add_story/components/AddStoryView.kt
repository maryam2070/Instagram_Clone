package com.example.instagramclone.presentation.story.add_story.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.instagramclone.utils.getImageLink
import com.intuit.sdp.R


@Composable
fun AddStoryView(userId: String, userName: String, onBtnClick: (String) -> Unit) {
    val content = remember {
        mutableStateOf("")
    }
    Column(Modifier.padding(dimensionResource(id = R.dimen._15sdp))) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                AsyncImage(
                    model = getImageLink(userId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen._50sdp))
                        .padding(dimensionResource(id = R.dimen._5sdp))
                        .clip(CircleShape),

                    contentScale = ContentScale.Crop
                )

                Text(
                    text = userName,
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen._4sdp))
                )
            }
            Button(onClick = {
                onBtnClick(content.value)
            }) {
                Text("Send")
            }
        }

        TextField(
            value = content.value,
            onValueChange = {
                content.value = it
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.Transparent),
            singleLine = false,
            placeholder = {
                Text(text = "What's happening today")
            },
        )

    }
}