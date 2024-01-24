package com.example.instagramclone.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.instagramclone.presentation.search.components.UserItem
import com.intuit.sdp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    val viewModel:SearchViewModel= hiltViewModel()

    var query by remember {
        mutableStateOf("")
    }


    Box(contentAlignment = Alignment.Center) {

        if (viewModel.uiState.value.isLoading) {
            BallZigZagProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
            )
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize(), content = {
            item {
                SearchBar(
                    query = query,
                    onQueryChange = {
                        query = it
                    },
                    active = false,
                    onSearch = {
                        viewModel.getUsersWithNameQuery(query)
                    },
                    onActiveChange = {},
                    modifier = Modifier.padding(dimensionResource(id = R.dimen._5sdp)),
                    placeholder = { Text(text = "Search") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = ""
                        )
                    },
                ) {
                }
            }
            items(viewModel.uiState.value.users) {
                UserItem(navController = navController, user = it)
            }
        })
    }

}

