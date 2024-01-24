package com.example.instagramclone.domain.use_cases.img_use_cases

import android.net.Uri
import com.example.instagramclone.domain.repository.StorageRepository
import javax.inject.Inject

class UploadImg @Inject constructor(
    private val repository: StorageRepository
) {
    operator fun invoke(uri: Uri, path: String) = repository.uploadImg(uri,path)
}