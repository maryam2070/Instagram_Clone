package com.example.instagramclone.domain.use_cases.chat_use_cases

import com.example.instagramclone.domain.repository.ChatRepository
import javax.inject.Inject

class GetChat @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(chatId:String) = repository.getChatMessages(chatId)
}