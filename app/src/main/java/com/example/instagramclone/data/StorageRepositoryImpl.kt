package com.example.instagramclone.data

import android.net.Uri
import com.example.instagramclone.domain.repository.StorageRepository
import com.example.instagramclone.utils.Constants
import com.example.instagramclone.utils.Response
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(private val storage: FirebaseStorage) :
    StorageRepository {
    override fun uploadImg(uri: Uri, path: String): Flow<Response<Boolean>> = callbackFlow {

        trySend(Response.Loading(true))
        try {
            storage.getReference(path).putFile(uri)
                .addOnSuccessListener {
                trySend(Response.Success(true))
            }.addOnFailureListener {

                trySend(Response.Error(it.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
            }
        } catch (e: Exception) {
            trySend(Response.Error(e.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
        }

        trySend(Response.Loading(false))
        awaitClose()
    }

    override fun deleteImg(path: String): Flow<Response<Boolean>> = callbackFlow {
        try {
            trySend(Response.Loading(true))
            storage.getReference(path)
                .delete()
                .addOnSuccessListener {
                    trySend(Response.Success(true))
                }.addOnFailureListener {

                    trySend(Response.Error(it.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
                }
        } catch (e: Exception) {
            trySend(Response.Error(e.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
        }
        trySend(Response.Loading(false))
        awaitClose()
    }

    override fun getImg(path: String): Flow<Response<Uri>> = callbackFlow {
        trySend(Response.Loading(true))
        try {
            storage.getReference(path).downloadUrl
                .addOnSuccessListener {
                    trySend(Response.Success(it))
                }.addOnFailureListener {
                    trySend(Response.Error(it.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
                }
        } catch (e: Exception) {
            trySend(Response.Error(e.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
        }
        trySend(Response.Loading(false))
        awaitClose()
    }
}