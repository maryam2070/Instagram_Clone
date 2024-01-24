package com.example.instagramclone.presentation.post_details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.instagramclone.domain.model.Comment
import com.example.instagramclone.utils.getImageLink
import com.intuit.sdp.R


@Composable
fun CommentItem(userId: String, comment: Comment, deleteComment: (String) -> Unit) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen._5sdp))
    ) {
        AsyncImage(
            model = getImageLink(comment.authorId),
            contentDescription = null,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen._40sdp))
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Column {
            Text(
                text = comment.authorName,
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen._5sdp)),
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp
            )
            Text(
                text = comment.content,
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen._5sdp)),
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._10ssp).value.sp
            )

        }
        if (userId == comment.authorId) {
            DropDownMenu {
                deleteComment(comment.id)

            }
        }
    }
}
