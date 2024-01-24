package com.example.instagramclone.presentation.story.add_story

data class AddStoryUiState(
    val isUploaded: Int = 0,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""

)