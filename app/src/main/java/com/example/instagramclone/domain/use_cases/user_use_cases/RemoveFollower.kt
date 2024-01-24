package com.example.instagramclone.domain.use_cases.user_use_cases

import com.example.instagramclone.domain.model.Friend
import com.example.instagramclone.domain.repository.UserRepository
import javax.inject.Inject

class RemoveFollower @Inject constructor(
    val repository: UserRepository
) {
    operator fun invoke(userId: String,follower: Friend) = repository.removeFollower(userId,follower)
}