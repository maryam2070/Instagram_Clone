package com.example.instagramclone.presentation.chat.chat_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ehsanmsz.mszprogressindicator.progressindicator.BallZigZagProgressIndicator
import com.example.instagramclone.presentation.chat.chat_list.components.UserItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(navController: NavController) {


    val viewModel: ChatListViewModel = hiltViewModel()

    var query by remember {
        mutableStateOf("")
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (viewModel.uiState.value.isLoading)
            BallZigZagProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
            )

        LazyColumn(Modifier.fillMaxSize()) {
            item {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                ) {


                    SearchBar(
                        query = query,
                        onQueryChange = {
                            query=it
                            viewModel.filterChats(it)
                        },
                        active=false,
                        onSearch = {},
                        onActiveChange = {},
                        modifier = Modifier.padding(dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)),
                        placeholder = { Text(text = "Search") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "") },) {

                    }
                }
            }

            items(viewModel.uiState.value.filteredChats) { item ->
                UserItem(navController, item)
            }
        }
    }
}

