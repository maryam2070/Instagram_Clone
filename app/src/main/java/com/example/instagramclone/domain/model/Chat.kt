package com.example.instagramclone.domain.model

data class Chat(
    val id: String="",
    val firstUserId: String="",
    val secondUserId: String="",
    val messages: List<Message> = emptyList()
)
