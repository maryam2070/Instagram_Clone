package com.example.instagramclone.domain.use_cases.user_use_cases

import com.example.instagramclone.domain.repository.UserRepository
import javax.inject.Inject

class GetUsersWithNameQuery @Inject constructor(
    val repository: UserRepository
) {
    operator fun invoke(query: String) = repository.getUsersWithNameQuery(query)
}