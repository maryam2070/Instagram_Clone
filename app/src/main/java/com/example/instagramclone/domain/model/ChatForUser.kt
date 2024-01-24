package com.example.instagramclone.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class ChatForUser(
    val id:String="",
    val friendNotificationToken:String="",
    val friendName:String="",
    val lastMessage:String="",
    val imgUrl:String="",
    @ServerTimestamp
    var lastMessageTime: Timestamp = Timestamp(Date())
)
