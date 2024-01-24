package com.example.instagramclone.domain.use_cases.notification_use_cases

import com.example.instagramclone.domain.repository.NotificationRepository
import javax.inject.Inject

class WriteNotification @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(
        senderId: String,
        receiverId: String,
        title: String,
        body: String,
        postId: String,
    ) =
        repository.writeNotification(senderId,receiverId,title, body,postId)
}
