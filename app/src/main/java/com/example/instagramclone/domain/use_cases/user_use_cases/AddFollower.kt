package com.example.instagramclone.domain.use_cases.user_use_cases

import com.example.instagramclone.domain.model.Friend
import com.example.instagramclone.domain.repository.UserRepository
import javax.inject.Inject

class AddFollower @Inject constructor(
    val repository: UserRepository
) {
    operator fun invoke(userId: String,userName:String,follower: Friend) = repository.addFollower(userId,userName,follower)
}