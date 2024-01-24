package com.example.instagramclone.data

import android.annotation.SuppressLint
import android.util.Log
import com.example.instagramclone.domain.model.Notification
import com.example.instagramclone.domain.model.PushNotification
import com.example.instagramclone.domain.repository.NotificationRepository
import com.example.instagramclone.utils.Constants
import com.example.instagramclone.utils.Constants.COLLECTION_NAME_USERS
import com.example.instagramclone.utils.Response
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val api: NotificationApi
) :
    NotificationRepository {
    override fun writeNotification(
        senderId: String,
        receiverId: String,
        title: String,
        body: String,
        postId: String,
    ) {
        try {
            val doc = firestore
                .collection(COLLECTION_NAME_USERS)
                .document(receiverId)
                .collection(Constants.COLLECTION_NAME_NOTIFICATIONS)
                .document()

            doc.set(
                mapOf(
                    Constants.ID_FIELD_NAME to doc.id,
                    Constants.TITLE_FIELD_NAME to title,
                    Constants.AUTHOR_ID_FIELD_NAME to senderId,
                    Constants.BODY_FIELD_NAME to body,
                    Constants.POST_ID_FIELD_NAME to postId
                )
            )
                .addOnSuccessListener {
                    Log.d("writeNotification", "Success")
                }.addOnFailureListener {
                    Log.d("writeNotification", it.message ?: Constants.UNKNOWN_ERROR_OCCURRED)
                }

        } catch (e: Exception) {
            Log.d("writeNotification", e.message ?: Constants.UNKNOWN_ERROR_OCCURRED)
        }
    }

    override fun getNotifications(userId: String): Flow<Response<List<Notification>>> =
        callbackFlow {
            trySend(Response.Loading(isLoading = true))
            val snapshotListener =
                firestore
                    .collection(COLLECTION_NAME_USERS)
                    .document(userId)
                    .collection(Constants.COLLECTION_NAME_NOTIFICATIONS)
                    .addSnapshotListener { snapshot, e ->
                        if (snapshot != null) {
                            val notificationList = snapshot.toObjects(Notification::class.java)
                            trySend(Response.Success(notificationList))
                        } else {
                            trySend(Response.Error(e?.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
                        }
                    }
            trySend(Response.Loading(isLoading = false))

            awaitClose {
                snapshotListener.remove()
            }
        }

    @SuppressLint("SuspiciousIndentation")
    override fun deleteNotifications(userId: String): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading(isLoading = true))

        val notificationsRef =
            firestore
                .collection(COLLECTION_NAME_USERS)
                .document(userId)
                .collection(Constants.COLLECTION_NAME_NOTIFICATIONS)

        notificationsRef
            .get()
            .addOnSuccessListener {

                firestore.runTransaction { transition ->
                    val notificationList = it.toObjects(Notification::class.java)
                    for (i in 0 until notificationList.size) {

                        transition.delete(notificationsRef.document(notificationList[i].id))
                    }
                    null
                }.addOnSuccessListener {

                }.addOnFailureListener {

                }
            }


        trySend(Response.Loading(isLoading = false))

        awaitClose {
        }
    }

    override fun postNotification(notification: PushNotification): Flow<Response<ResponseBody>> =
        flow {
            val response = api.postNotification(notification)

            response.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: retrofit2.Response<ResponseBody>
                ) {
                    Log.d("postNotification","success")
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("postNotification",t.message.toString())
                }

            })

        }

}