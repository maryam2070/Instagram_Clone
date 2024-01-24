package com.example.instagramclone.presentation.discover

import com.example.instagramclone.domain.model.User

data class AddNewFollowingUiState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String = ""
)