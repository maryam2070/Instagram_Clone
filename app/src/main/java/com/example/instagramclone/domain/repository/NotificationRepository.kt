package com.example.instagramclone.domain.repository

import com.example.instagramclone.domain.model.Notification
import com.example.instagramclone.domain.model.PushNotification
import com.example.instagramclone.utils.Response
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface NotificationRepository {

    fun writeNotification(
        senderId: String,
        receiverId: String,
        title: String,
        body: String,
        postId: String
    )

    fun getNotifications(userId: String): Flow<Response<List<Notification>>>

    fun deleteNotifications(userId: String): Flow<Response<Boolean>>


    fun postNotification(
        notification: PushNotification
    ): Flow<Response<ResponseBody>>

}