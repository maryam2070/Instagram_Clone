package com.example.instagramclone.domain.repository

import android.graphics.Bitmap

fun interface SaveImageRepository {

    fun saveImage(bitmap: Bitmap)
}