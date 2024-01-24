package com.example.instagramclone.presentation.discover

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ehsanmsz.mszprogressindicator.progressindicator.BallZigZagProgressIndicator
import com.example.instagramclone.domain.model.Friend
import com.example.instagramclone.presentation.discover.components.FollowingItem
import com.intuit.sdp.R


@Composable
fun AddNewFollowingScreen(
    navController: NavController
) {

    val viewModel: AddNewFollowingViewModel = hiltViewModel()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (viewModel.uiState.value.isLoading)
            BallZigZagProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
            )
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            viewModel.users.value.data?.let {
                item {
                    Text(
                        "Discover New Friends",
                        style = TextStyle(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(red = 182, green = 217, blue = 247),
                                    Color(red = 227, green = 161, blue = 207),
                                    Color(red = 110, green = 26, blue = 103)
                                ),
                                tileMode = TileMode.Mirror
                            ),
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(id = R.dimen._15sdp)),
                        textAlign = TextAlign.Center,
                        fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._20ssp).value.sp
                    )
                }
                items(it) { user ->
                    FollowingItem(navController,user) {
                        viewModel.addFollow(
                            viewModel.userId.value,
                            viewModel.userName.value,
                            Friend(user.name, user.id)
                        )
                    }
                }
            }
        }
    }
}
