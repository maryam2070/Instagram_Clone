package com.example.instagramclone.data

import com.example.instagramclone.domain.model.User
import com.example.instagramclone.domain.repository.AuthRepository
import com.example.instagramclone.utils.Constants
import com.example.instagramclone.utils.Constants.UNKNOWN_ERROR_OCCURRED
import com.example.instagramclone.utils.Response
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    val auth: FirebaseAuth,
    val firestore: FirebaseFirestore
) : AuthRepository {

    override fun isUserAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    override fun getFirebaseAuthState(): Flow<Boolean> = callbackFlow {
        val authListener = FirebaseAuth.AuthStateListener {
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authListener)
        awaitClose {
            auth.removeAuthStateListener(authListener)
        }
    }

    override fun firebaseSignIn(email: String, password: String): Flow<Response<FirebaseUser>> =
        callbackFlow {
            try {
                trySend(Response.Loading(true))
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        it.user?.let {
                            trySend(Response.Success(it))
                        }
                    }.addOnFailureListener {
                        trySend(
                            Response.Error(
                                message = it.localizedMessage ?: UNKNOWN_ERROR_OCCURRED
                            )
                        )
                    }
            } catch (e: Exception) {
                trySend(Response.Error(message = e.localizedMessage ?: UNKNOWN_ERROR_OCCURRED))
            }
            awaitClose()
        }

    override fun firebaseSignOut(): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading(true))
            auth.signOut()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(message = e.localizedMessage ?: UNKNOWN_ERROR_OCCURRED))
        }
    }

    override fun firebaseSignUp(
        email: String,
        password: String,
        username: String
    ): Flow<Response<FirebaseUser>> = callbackFlow {
        try {
            trySend(Response.Loading(true))
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                auth.currentUser?.let { user ->
                    val profileUpdates = userProfileChangeRequest {
                        displayName = username
                    }
                    user.updateProfile(profileUpdates).addOnSuccessListener {

                        FirebaseMessaging.getInstance().token.addOnSuccessListener {
                            val userId = auth.currentUser?.uid!!
                            val obj =
                                User(
                                    name = username,
                                    id = userId,
                                    email = email,
                                    imageUrl = userId,
                                    notificationToken = it
                                )


                            firestore.collection(Constants.COLLECTION_NAME_USERS).document(userId)
                                .set(obj)
                                .addOnSuccessListener {
                                    verifyEmail()
                                    trySend(Response.Success(auth.currentUser))

                                }
                                .addOnFailureListener {
                                    trySend(
                                        Response.Error(
                                            message = it.localizedMessage ?: UNKNOWN_ERROR_OCCURRED
                                        )
                                    )
                                }
                        }.addOnFailureListener {
                            trySend(
                                Response.Error(
                                    message = it.localizedMessage ?: UNKNOWN_ERROR_OCCURRED
                                )
                            )
                        }
                    }.addOnFailureListener {
                        trySend(
                            Response.Error(
                                message = it.localizedMessage ?: UNKNOWN_ERROR_OCCURRED
                            )
                        )
                    }

                }
            }
        } catch (e: Exception) {
            trySend(Response.Error(message = e.localizedMessage ?: UNKNOWN_ERROR_OCCURRED))
        }
        awaitClose()
    }

    override fun getUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun changeEmail(
        curEmail: String,
        newEmail: String,
        curPassword: String
    ): Flow<Response<Boolean>> = callbackFlow {
        reAuthenticateUser(curEmail, curPassword)
            ?.addOnSuccessListener {
                auth.currentUser?.let { user ->
                    user.updateEmail(newEmail)
                        .addOnSuccessListener {
                            trySend(Response.Success(true))
                        }.addOnFailureListener {
                            trySend(Response.Error(it.message ?: UNKNOWN_ERROR_OCCURRED))
                        }
                }
            }
            ?.addOnFailureListener {
                trySend(Response.Error(it.message ?: UNKNOWN_ERROR_OCCURRED))

            }

        awaitClose()
    }

    override fun changePassword(email: String, password: String): Flow<Response<Boolean>> =
        callbackFlow {
         /*   auth.currentUser?.let {
                it.updatePassword(password)
                    .addOnCompleteListener {
                        callbackFlow<Flow<Response<Boolean>>> {
                            trySend(reAuthenticateUser(email, password))
                            awaitClose()
                        }
                    }.addOnFailureListener {
                        trySend(Response.Error(it.message ?: UNKNOWN_ERROR_OCCURRED))
                    }
*
            }*/
            awaitClose()
        }

    override suspend fun reAuthenticateUser(email: String, password: String): Task<Void>? {
        val credential = EmailAuthProvider
            .getCredential(email, password)

        return auth.currentUser?.reauthenticate(credential)
    }

    override fun verifyEmail() {
        auth.currentUser?.let {
            it.sendEmailVerification().addOnFailureListener {
                verifyEmail()
            }
        }
    }

}