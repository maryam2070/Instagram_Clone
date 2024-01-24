package com.example.instagramclone.domain.use_cases.chat_use_cases

import com.example.instagramclone.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessage @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(
        senderId: String,
        senderName: String,
        senderNotificationToken: String,
        receiverId: String,
        receiverName: String,
        receiverNotificationToken: String,
        message: String,
        chatId: String) = repository.sendMessage(senderId, senderName, senderNotificationToken, receiverId, receiverName, receiverNotificationToken, message, chatId)
}