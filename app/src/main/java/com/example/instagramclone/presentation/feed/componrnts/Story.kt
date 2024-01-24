package com.example.instagramclone.presentation.feed.componrnts

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.instagramclone.domain.model.Story
import com.example.instagramclone.utils.getImageLink
import com.intuit.sdp.R


@Composable
fun StoryView(
    story: Story,
    onClick:()->Unit
) {

    Box(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen._5sdp))
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.TopCenter,
    ) {

            var colors = listOf(
                Color(red = 108, green = 25, blue = 103),
                Color(red = 227, green = 157, blue = 209),
                Color(red = 184, green = 217, blue = 248),
            )
            if(story.isOpened)
                colors = listOf(Color.LightGray, Color.Gray,Color.DarkGray)


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = getImageLink(story.userId),
                contentDescription = null,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen._70sdp))
                    .clip(CircleShape)
                    .border(width = 5.dp, brush =  Brush.linearGradient(colors), shape = CircleShape)
                    ,
                contentScale = ContentScale.Crop
            )
            Text(
                text = story.userName,
            )
        }
    }
}

