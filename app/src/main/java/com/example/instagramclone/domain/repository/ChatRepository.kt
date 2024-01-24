package com.example.instagramclone.domain.repository

import com.example.instagramclone.domain.model.ChatForUser
import com.example.instagramclone.domain.model.Message
import com.example.instagramclone.utils.Response
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun sendMessage(
        senderId: String,
        senderName: String,
        senderNotificationToken: String,
        receiverId: String,
        receiverName: String,
        receiverNotificationToken: String,
        message: String,
        chatId: String
    ): Flow<Response<String>>

    fun getChatMessages(chatId: String): Flow<Response<List<Message>>>
    fun getChats(userId: String): Flow<Response<List<ChatForUser>>>
}