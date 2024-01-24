package com.example.instagramclone.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Message(
    val id: String="",
    @ServerTimestamp
    val createdAt: Timestamp= Timestamp(Date()),
    val content: String="",
    val authorId: String=""
)
fun Message.isFromMe(curId:String,authorId:String)= (authorId == curId)