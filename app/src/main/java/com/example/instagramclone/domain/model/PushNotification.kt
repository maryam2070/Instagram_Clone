package com.example.instagramclone.domain.model

data class PushNotification(
    val to:String,
    val data: NotificationData,
    val priority:String= "high"
)
data class NotificationData(
    val title:String="",
    val body:String="",
    val type:Int,
    val postId:String,
    val chatItem:String,
    val senderId:String,
    val receiverId:String,
)