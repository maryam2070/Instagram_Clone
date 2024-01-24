package com.example.instagramclone.presentation.search

import com.example.instagramclone.domain.model.User

data class SearchUiState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
)