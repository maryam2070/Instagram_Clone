package com.example.instagramclone.domain.use_cases.auth_use_cases

import com.example.instagramclone.domain.repository.AuthRepository
import javax.inject.Inject

class FirebaseSignUp @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke (email :String, password:String,username:String) =repository.firebaseSignUp(email, password,username)

}