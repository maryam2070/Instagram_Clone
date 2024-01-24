package com.example.instagramclone.presentation.chat.chat_details.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instagramclone.domain.model.Message
import com.example.instagramclone.domain.model.isFromMe
import com.example.instagramclone.utils.getTimeFormat


@Composable
fun ChatItem(curUserId:String,message: Message) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {

        Box(
            modifier = Modifier
                .align(if (message.isFromMe(curUserId,message.authorId)) Alignment.End else Alignment.Start)
                .border(
                    color = Color.White, width = 1.dp, shape = RoundedCornerShape(
                        topStart = 48f,
                        topEnd = 48f,
                        bottomStart = if (message.isFromMe(curUserId,message.authorId)) 48f else 0f,
                        bottomEnd = if (message.isFromMe(curUserId,message.authorId)) 0f else 48f
                    )
                )
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 48f,
                        topEnd = 48f,
                        bottomStart = if (message.isFromMe(curUserId,message.authorId)) 48f else 0f,
                        bottomEnd = if (message.isFromMe(curUserId,message.authorId)) 0f else 48f
                    )
                )
                .padding(16.dp)

        ) {
            Text(text = message.content)
        }

        Text(
            text = getTimeFormat(message.createdAt.toDate()),
            //(message.createdAt.toDate().minutes.toString()+" : "+message.createdAt.toDate().hours).toString(),
            Modifier
                .align(if (message.isFromMe(curUserId,message.authorId)) Alignment.End else Alignment.Start)
                .padding(2.dp),
            fontSize = 14.sp
        )

    }
}
