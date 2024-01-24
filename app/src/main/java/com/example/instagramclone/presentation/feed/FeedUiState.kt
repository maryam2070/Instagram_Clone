package com.example.instagramclone.presentation.feed

import com.example.instagramclone.domain.model.Post
import com.example.instagramclone.domain.model.Story

data class FeedUiState(
    val stories: List<Story> = emptyList(),
    val feed: List<Post> = emptyList(),
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String = ""
)