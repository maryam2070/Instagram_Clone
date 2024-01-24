package com.example.instagramclone.domain.use_cases.notification_use_cases

import com.example.instagramclone.domain.repository.NotificationRepository
import javax.inject.Inject

class GetNotifications @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(
        userId: String
    ) =
        repository.getNotifications(userId)

}
