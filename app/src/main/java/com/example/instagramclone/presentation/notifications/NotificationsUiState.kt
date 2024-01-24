package com.example.instagramclone.presentation.notifications

import com.example.instagramclone.domain.model.Notification

data class NotificationsUiState(
    val notifications: List<Notification> = emptyList(),
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String = ""
)