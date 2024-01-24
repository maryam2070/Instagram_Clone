package com.example.instagramclone.domain.use_cases.user_use_cases

import com.example.instagramclone.domain.repository.UserRepository
import javax.inject.Inject

class CheckIfFriendExist @Inject constructor(
    val repository: UserRepository
) {
    operator fun invoke(userId: String,friendId:String) = repository.checkIfFriendExist(userId,friendId)
}