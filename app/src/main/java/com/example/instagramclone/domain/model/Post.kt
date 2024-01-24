package com.example.instagramclone.domain.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class Post(
    val postId :String = "",
    val postDescription : String="",
    @ServerTimestamp
    val time : Timestamp = Timestamp(Date()),
    val userId : String = "",
    val userName : String = "",
    val notificationToken:String="",
    val likes:List<String> = emptyList()
): Parcelable