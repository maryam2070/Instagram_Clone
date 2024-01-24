package com.example.instagramclone.presentation.add_post

import com.example.instagramclone.domain.model.PickedImage


data class AddPostUiState(
    val isUploaded: Int = 0,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val photos:List<PickedImage> = emptyList()
)