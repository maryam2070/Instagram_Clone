package com.example.instagramclone.domain.repository

import com.example.instagramclone.domain.model.PickedImage

fun interface ExternalStorageRepository {

    suspend fun loadLatestImagesFromExternalStorage(): List<PickedImage>

}