package com.example.instagramclone.presentation.profile

import com.example.instagramclone.domain.model.Post
import com.example.instagramclone.domain.model.ProfileNavigationType
import com.example.instagramclone.domain.model.User

data class ProfileUiState(
    val user: User = User(),
    val posts: List<Post> = emptyList<Post>(),
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val navigationType: ProfileNavigationType = ProfileNavigationType.USER_PROFILE,
    val signedOut:Boolean=false
)