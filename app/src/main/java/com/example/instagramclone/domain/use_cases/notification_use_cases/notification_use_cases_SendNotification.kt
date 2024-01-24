package com.example.instagramclone.domain.use_cases.notification_use_cases

import com.example.instagramclone.domain.model.PushNotification
import com.example.instagramclone.domain.repository.NotificationRepository
import javax.inject.Inject

class SendNotification  @Inject constructor(
    private val repository: NotificationRepository
) {
     operator fun invoke(notification: PushNotification) =
        repository.postNotification(notification)
}
