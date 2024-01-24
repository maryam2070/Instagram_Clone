package com.example.instagramclone.presentation.chat.chat_list

import com.example.instagramclone.domain.model.ChatForUser

data class ChatListUiState(
    val chats: List<ChatForUser> = emptyList<ChatForUser>(),
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val filteredChats:List<ChatForUser> = emptyList<ChatForUser>()
)