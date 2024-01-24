package com.example.instagramclone.presentation.story.story_details

data class StoryDetailsUiState(
    val isDeleted: Boolean=false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""

)