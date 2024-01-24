package com.example.instagramclone.domain.use_cases.user_use_cases

import com.example.instagramclone.domain.repository.UserRepository
import javax.inject.Inject

class SetUserDetails @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(
        userId: String,data:Map<String,Any>
    ) = repository.setUserDetails(
        userId, data
    )


}