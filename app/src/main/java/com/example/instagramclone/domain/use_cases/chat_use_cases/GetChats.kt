package com.example.instagramclone.domain.use_cases.chat_use_cases

import com.example.instagramclone.domain.repository.ChatRepository
import javax.inject.Inject

class GetChats @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(userId:String) = repository.getChats(userId)
}