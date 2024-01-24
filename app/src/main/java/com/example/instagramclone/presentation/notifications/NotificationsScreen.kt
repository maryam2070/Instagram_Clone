package com.example.instagramclone.presentation.notifications

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.instagramclone.presentation.notifications.components.NotificationItem

@Composable
fun NotificationsScreen(navController: NavController) {

    val viewModel:NotificationsViewModel= hiltViewModel()

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        if (viewModel.uiState.value.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(
                    dimensionResource(id = com.intuit.sdp.R.dimen._80sdp)
                )
            )
        } else {

            Column(Modifier.fillMaxSize()) {
                Text(
                    text = "Notifications",
                    modifier = Modifier
                        .padding( dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)),
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._18ssp).value.sp
                )
                val listState = rememberLazyListState()
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items(viewModel.uiState.value.notifications) { item ->
                        NotificationItem(navController,item)

                    }
                }
            }
        }
    }
}
