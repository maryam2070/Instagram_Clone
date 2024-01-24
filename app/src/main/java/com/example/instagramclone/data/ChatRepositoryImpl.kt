package com.example.instagramclone.data

import com.example.instagramclone.domain.model.ChatForUser
import com.example.instagramclone.domain.model.Message
import com.example.instagramclone.domain.repository.ChatRepository
import com.example.instagramclone.utils.Constants
import com.example.instagramclone.utils.Constants.AUTHOR_ID_FIELD_NAME
import com.example.instagramclone.utils.Constants.COLLECTION_NAME_CHATS
import com.example.instagramclone.utils.Constants.COLLECTION_NAME_MESSAGES
import com.example.instagramclone.utils.Constants.COLLECTION_NAME_USERS
import com.example.instagramclone.utils.Constants.CONTENT_FIELD_NAME
import com.example.instagramclone.utils.Constants.CREATED_AT_FIELD_NAME
import com.example.instagramclone.utils.Constants.ID_FIELD_NAME
import com.example.instagramclone.utils.Constants.LAST_MESSAGE_FIELD_NAME
import com.example.instagramclone.utils.Constants.LAST_MESSAGE_TIME_FIELD_NAME
import com.example.instagramclone.utils.Response
import com.google.firebase.firestore.FieldValue.serverTimestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val fbStore: FirebaseFirestore
) : ChatRepository {
    override fun sendMessage(
        senderId: String,
        senderName: String,
        senderNotificationToken: String,
        receiverId: String,
        receiverName: String,
        receiverNotificationToken: String,
        message: String,
        chatId: String
    ): Flow<Response<String>> = callbackFlow {
        val msgObj = mutableMapOf<String, Any>()
        msgObj[CONTENT_FIELD_NAME] = message
        msgObj[CREATED_AT_FIELD_NAME] = serverTimestamp()
        msgObj[AUTHOR_ID_FIELD_NAME] = senderId

        val chatDoc = fbStore.collection(COLLECTION_NAME_CHATS).document(chatId)
        msgObj[ID_FIELD_NAME] = chatId


        val senderChatDoc = fbStore.collection(COLLECTION_NAME_USERS)
            .document(senderId)
            .collection(COLLECTION_NAME_CHATS)
            .document(chatDoc.id)

        val receiverChatDoc = fbStore.collection(COLLECTION_NAME_USERS)
            .document(receiverId)
            .collection(COLLECTION_NAME_CHATS)
            .document(chatDoc.id)

        val mgsDoc = chatDoc.collection(COLLECTION_NAME_MESSAGES).document()

        fbStore.runTransaction() { transaction ->
            transaction.set(mgsDoc, msgObj)

            transaction.set(
                senderChatDoc,
                mapOf(
                    "id" to chatDoc.id,
                    "friendNotificationToken" to receiverNotificationToken,
                    "friendName" to receiverName,
                    "lastMessage" to message,
                    "imgUrl" to receiverId,
                    "lastMessageTime" to com.google.firebase.Timestamp.now()
                )
            )


            transaction.set(
                receiverChatDoc,
                mapOf(
                    "id" to chatDoc.id,
                    "friendNotificationToken" to senderNotificationToken,
                    "friendName" to senderName,
                    "lastMessage" to message,
                    "imgUrl" to senderId,
                    "lastMessageTime" to com.google.firebase.Timestamp.now()
                )
            )

            transaction.update(receiverChatDoc, LAST_MESSAGE_FIELD_NAME, message)
            transaction.update(receiverChatDoc, LAST_MESSAGE_TIME_FIELD_NAME, serverTimestamp())


            null
        }.addOnSuccessListener {
            trySend(Response.Success(chatDoc.id))
        }.addOnFailureListener {
            trySend(Response.Error(it.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
        }

        awaitClose()
    }

    override fun getChatMessages(chatId: String): Flow<Response<List<Message>>> = callbackFlow {
        val snapShotListener = fbStore.collection(COLLECTION_NAME_CHATS)
            .document(chatId)
            .collection(COLLECTION_NAME_MESSAGES)
            .addSnapshotListener { shot, error ->
                if (shot != null) {
                    val data = shot.map {
                        it.toObject(Message::class.java)
                    }

                    trySend(Response.Success(data))
                } else {
                    trySend(Response.Error(error?.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
                }

            }
        awaitClose {
            snapShotListener.remove()
        }
    }

    override fun getChats(userId: String): Flow<Response<List<ChatForUser>>> = callbackFlow {

        val snapshotListener = fbStore.collection(COLLECTION_NAME_USERS)
            .document(userId)
            .collection(COLLECTION_NAME_CHATS)
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null) {
                    val chats = snapshot.toObjects(ChatForUser::class.java)
                    trySend(Response.Success(chats))
                } else {
                    trySend(Response.Error(e?.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
                }
            }

        awaitClose {
            snapshotListener.remove()
        }
    }


}