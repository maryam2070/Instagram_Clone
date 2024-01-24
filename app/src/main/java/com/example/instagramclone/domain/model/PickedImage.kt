package com.example.instagramclone.domain.model

import android.net.Uri

data class PickedImage(
    val id: Long,
    val name: String,
    val width: Int,
    val height: Int,
    val contentUri: Uri,
)
