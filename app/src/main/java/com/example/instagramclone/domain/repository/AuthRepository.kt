package com.example.instagramclone.domain.repository

import com.example.instagramclone.utils.Response
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun isUserAuthenticated(): Boolean

    fun getFirebaseAuthState(): Flow<Boolean>

    fun firebaseSignIn(email: String, password: String): Flow<Response<FirebaseUser>>

    fun firebaseSignOut(): Flow<Response<Boolean>>

    fun firebaseSignUp(email: String, password: String, username: String): Flow<Response<FirebaseUser>>

    fun getUser(): FirebaseUser?

    fun changeEmail(
        curEmail: String,
        newEmail: String,
        curPassword: String,
    ): Flow<Response<Boolean>>

    fun changePassword(email: String, password: String): Flow<Response<Boolean>>

    suspend fun reAuthenticateUser(email: String, password: String): Task<Void>?

    fun verifyEmail()
}