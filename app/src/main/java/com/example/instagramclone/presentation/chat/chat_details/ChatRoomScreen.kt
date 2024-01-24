package com.example.instagramclone.presentation.chat.chat_details


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ehsanmsz.mszprogressindicator.progressindicator.BallZigZagProgressIndicator
import com.example.instagramclone.domain.model.ChatForUser
import com.example.instagramclone.presentation.chat.chat_details.components.ChatBox
import com.example.instagramclone.presentation.chat.chat_details.components.ChatItem
import com.example.instagramclone.utils.getImageLink
import com.intuit.sdp.R


@Composable
fun ChatRoomScreen(
    chatItem: ChatForUser
) {

    val viewModel: ChatDetailsViewModel = hiltViewModel()

    viewModel.getChat(chatItem.id)

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        if (viewModel.uiState.value.isLoading)
            BallZigZagProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
            )
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (topBar, messages, chatBox) = createRefs()
            val listState = rememberLazyListState()


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .constrainAs(topBar) {
                        top.linkTo(parent.top)
                        bottom.linkTo(messages.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = getImageLink(chatItem.imgUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen._60sdp))
                            .padding(dimensionResource(id = R.dimen._10sdp))
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Text(text = chatItem.friendName, Modifier.padding(10.dp))

                }
                Divider(thickness = 1.dp)
            }
            LazyColumn(
                reverseLayout = true,
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(messages) {
                        top.linkTo(topBar.bottom)
                        bottom.linkTo(chatBox.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                    },
                contentPadding = PaddingValues(16.dp)
            ) {
                items(viewModel.uiState.value.messages) { item ->
                     ChatItem(viewModel.userId.value,item)
                }
            }
            ChatBox(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(chatBox) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {

                viewModel.sendMessage(
                    senderId = viewModel.userId.value,
                    senderName = viewModel.userName.value,
                    senderNotificationToken = viewModel.userToken.value,
                    receiverId = chatItem.imgUrl,
                    receiverName = chatItem.friendName,
                    receiverNotificationToken = chatItem.friendNotificationToken,
                    message = it,
                    chatItem = chatItem
                )
            }
        }
    }
}
