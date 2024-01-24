package com.example.instagramclone.domain.repository

import android.net.Uri
import com.example.instagramclone.utils.Response
import kotlinx.coroutines.flow.Flow

interface StorageRepository {
    fun uploadImg(uri: Uri, path: String): Flow<Response<Boolean>>
    fun deleteImg(path: String): Flow<Response<Boolean>>
    fun getImg(path:String): Flow<Response<Uri>>
}