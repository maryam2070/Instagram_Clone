package com.example.instagramclone.data

import android.util.Log
import com.example.instagramclone.domain.model.Friend
import com.example.instagramclone.domain.model.User
import com.example.instagramclone.domain.repository.UserRepository
import com.example.instagramclone.utils.Constants
import com.example.instagramclone.utils.Constants.COLLECTION_NAME_FOLLOWERS
import com.example.instagramclone.utils.Constants.COLLECTION_NAME_FOLLOWING
import com.example.instagramclone.utils.Constants.COLLECTION_NAME_USERS
import com.example.instagramclone.utils.Constants.FOLLOWERS_FIELD_NAME
import com.example.instagramclone.utils.Constants.FOLLOWING_FIELD_NAME
import com.example.instagramclone.utils.Constants.ID_FIELD_NAME
import com.example.instagramclone.utils.Constants.NAME_FIELD_NAME
import com.example.instagramclone.utils.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class UserRepositoryImp @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UserRepository {

    override fun getUsers(ids: List<String>): Flow<Response<List<User>>> = callbackFlow {

        lateinit var ref: Query

        if (ids.isNotEmpty()) {
            ref = firebaseFirestore.collection(COLLECTION_NAME_USERS)
                .whereNotIn("id", ids)
        } else {
            ref = firebaseFirestore.collection(COLLECTION_NAME_USERS)
        }

        val snapshotListener = ref
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null) {
                    val user = snapshot.toObjects(User::class.java)
                    trySend(Response.Success(data = user))
                } else {
                    trySend(Response.Error(e?.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
                }
            }
        awaitClose {
            snapshotListener.remove()
        }

    }

    override fun getUserDetails(userId: String): Flow<Response<User>> = callbackFlow {
        val snapshotListener = firebaseFirestore.collection(COLLECTION_NAME_USERS)
            .document(userId).addSnapshotListener { snapshot, e ->
                if (snapshot != null) {
                    val user = snapshot.toObject(User::class.java)
                    trySend(Response.Success(data = user))
                } else {
                    trySend(Response.Error(e?.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
                }
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun getUsersWithNameQuery(query: String): Flow<Response<List<User>>> = callbackFlow {

        val ref = firebaseFirestore.collection(COLLECTION_NAME_USERS)

        val snapshotListener = ref
            .whereGreaterThanOrEqualTo("name", query)
            .whereLessThanOrEqualTo("name", "${query}\uF7FF")
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null) {
                    val user = snapshot.toObjects(User::class.java)
                    trySend(Response.Success(data = user))
                } else {
                    trySend(Response.Error(e?.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
                }
            }
        awaitClose {
            snapshotListener.remove()
        }

    }

    override fun setUserDetails(
        userId: String,
        data: Map<String, Any>
    ): Flow<Response<Boolean>> = callbackFlow {
        try {
            firebaseFirestore.collection(COLLECTION_NAME_USERS).document(userId)
                .set(data)
                .addOnSuccessListener {
                    trySend(Response.Success(true))
                }.addOnFailureListener {
                    trySend(Response.Error(it.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
                }
        } catch (e: Exception) {
            trySend(Response.Error(e.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
        }

        awaitClose()
    }

    private fun updateUserName(name: String) {
        val user = auth.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }

        user!!.updateProfile(profileUpdates)
            .addOnSuccessListener {
                Log.d("updateUserName", "success")
            }.addOnFailureListener {
                Log.d(
                    "updateUserName",
                    it.message ?: Constants.UNKNOWN_ERROR_OCCURRED
                )
            }
    }

    override fun updateUserDetails(
        userId: String,
        data: Map<String, Any>
    ): Flow<Response<Boolean>> = callbackFlow {
        try {

            updateUserName(data["name"].toString())

            firebaseFirestore.collection(COLLECTION_NAME_USERS).document(userId).update(data)
                .addOnSuccessListener {
                    trySend(Response.Success(true))
                }.addOnFailureListener {
                    trySend(Response.Error(it.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
                }
        } catch (e: Exception) {
            trySend(Response.Error(e.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
        }
        awaitClose()
    }

    override fun getFollowers(userId: String): Flow<Response<List<Friend>>> = callbackFlow {
        val snapshotListener = firebaseFirestore.collection(COLLECTION_NAME_USERS)
            .document(userId).collection(COLLECTION_NAME_FOLLOWERS)
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null) {
                    val followers = snapshot.toObjects(Friend::class.java)
                    trySend(Response.Success(data = followers))
                } else {
                    trySend(Response.Error(e?.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
                }
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun getFollowing(userId: String): Flow<Response<List<Friend>>> = callbackFlow {
        val snapshotListener = firebaseFirestore.collection(COLLECTION_NAME_USERS)
            .document(userId).collection(COLLECTION_NAME_FOLLOWING)
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null) {
                    val following = snapshot.toObjects(Friend::class.java)
                    trySend(Response.Success(data = following))
                } else {
                    trySend(Response.Error(e?.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
                }
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun addFollower(
        userId: String,
        userName: String,
        follower: Friend
    ): Flow<Response<Boolean>> = callbackFlow {
        try {
            val userRef = firebaseFirestore
                .collection(COLLECTION_NAME_USERS)
                .document(userId)

            val followingDocRef = userRef
                .collection(COLLECTION_NAME_FOLLOWING)
                .document(follower.id)


            val friendUserRef = firebaseFirestore
                .collection(COLLECTION_NAME_USERS)
                .document(follower.id)

            val followersDocRef = friendUserRef
                .collection(COLLECTION_NAME_FOLLOWERS)
                .document(follower.id)

            firebaseFirestore.runTransaction() { transaction ->
                //add one following
                transaction.set(
                    followingDocRef,
                    mapOf(ID_FIELD_NAME to follower.id, NAME_FIELD_NAME to follower.name)
                )
                transaction.update(userRef, FOLLOWING_FIELD_NAME, FieldValue.increment(1))


                //add on followers
                transaction.set(
                    followersDocRef,
                    mapOf(ID_FIELD_NAME to userId, NAME_FIELD_NAME to userName)
                )
                transaction.update(friendUserRef, FOLLOWERS_FIELD_NAME, FieldValue.increment(1))

                // Success
                null
            }
                .addOnSuccessListener {
                    trySend(Response.Success(true))
                }.addOnFailureListener {
                    trySend(Response.Error(it.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
                }
        } catch (e: Exception) {
            trySend(Response.Error(e.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
        }
        awaitClose()
    }

    override fun removeFollower(
        userId: String,
        follower: Friend
    ): Flow<Response<Boolean>> = callbackFlow {
        try {
            val userRef = firebaseFirestore
                .collection(COLLECTION_NAME_USERS)
                .document(userId)

            val followingDocRef = userRef
                .collection(COLLECTION_NAME_FOLLOWING)
                .document(follower.id)


            val friendUserRef = firebaseFirestore
                .collection(COLLECTION_NAME_USERS)
                .document(follower.id)

            val followersDocRef = friendUserRef
                .collection(COLLECTION_NAME_FOLLOWERS)
                .document(follower.id)

            firebaseFirestore.runTransaction() { transaction ->
                //add one following
                transaction.delete(followingDocRef)
                transaction.update(userRef, FOLLOWING_FIELD_NAME, FieldValue.increment(-1))


                //add on followers
                transaction.delete(followersDocRef)
                transaction.update(friendUserRef, FOLLOWERS_FIELD_NAME, FieldValue.increment(-1))

                // Success
                null
            }
                .addOnSuccessListener {
                    trySend(Response.Success(true))
                }.addOnFailureListener {
                    trySend(Response.Error(it.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
                }
        } catch (e: Exception) {
            trySend(Response.Error(e.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
        }
        awaitClose()
    }

    override fun checkIfFriendExist(userId: String, friendId: String): Flow<Response<Boolean>> =
        callbackFlow {
            val snapshotListener = firebaseFirestore
                .collection(COLLECTION_NAME_USERS)
                .document(userId)
                .collection(COLLECTION_NAME_FOLLOWING)
                .document(friendId)
                .addSnapshotListener { snapshot, e ->
                    if (snapshot != null) {
                        if (snapshot.exists())
                            trySend(Response.Success(true))
                        else
                            trySend(Response.Success(false))
                    } else {
                        trySend(Response.Error(e?.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
                    }
                }
            awaitClose {
                snapshotListener.remove()
            }
        }
}