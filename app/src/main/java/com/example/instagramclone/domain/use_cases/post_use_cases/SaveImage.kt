package com.example.instagramclone.domain.use_cases.post_use_cases

import android.graphics.Bitmap
import com.example.instagramclone.domain.repository.SaveImageRepository
import javax.inject.Inject

class SaveImage @Inject constructor(
    private val repository: SaveImageRepository
) {
    operator fun invoke(bitmap: Bitmap) =
        repository.saveImage(bitmap)
}