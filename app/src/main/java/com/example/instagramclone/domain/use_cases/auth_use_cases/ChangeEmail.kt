package com.example.instagramclone.domain.use_cases.auth_use_cases

import com.example.instagramclone.domain.repository.AuthRepository
import javax.inject.Inject

class ChangeEmail  @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke( curEmail: String,
                         newEmail: String,
                         curPassword: String
    ) = repository.changeEmail(curEmail, newEmail,curPassword,)

}