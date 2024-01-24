package com.example.instagramclone.presentation.users_list

import com.example.instagramclone.domain.model.Friend

data class UsersListListUiState(
    val users: List<Friend> = emptyList(),
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String = ""
)