package com.example.instagramclone.domain.model

data class User(
    val name: String = "",
    val id: String = "",
    val email: String = "",
    val password: String = "",
    val imageUrl: String = "",
    val following: Int=0,
    val followers: Int=0,
    val chats:List<String> = emptyList(),
    val totalPosts: Int = 0,
    val bio: String = "",
    val url: String = "",
    val notificationToken:String=""
)

