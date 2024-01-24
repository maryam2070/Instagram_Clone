package com.example.instagramclone.presentation.chat.chat_details

import com.example.instagramclone.domain.model.Message

data class ChatDetailsUiState(
    val messages: List<Message> = emptyList<Message>(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val chatId:String=""
)