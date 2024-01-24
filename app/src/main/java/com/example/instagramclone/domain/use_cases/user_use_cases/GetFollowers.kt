package com.example.instagramclone.domain.use_cases.user_use_cases

import com.example.instagramclone.domain.repository.UserRepository
import javax.inject.Inject

class GetFollowers @Inject constructor(
    val repository: UserRepository
) {
    operator fun invoke(userId: String) = repository.getFollowers(userId = userId)
}