package com.example.instagramclone.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Story(
    val id: String = "",
    val content: String = "",
    val userId:String="",
    val userName:String="",
    @ServerTimestamp
    val time: Timestamp= Timestamp.now(),
    val isOpened:Boolean=false
)
