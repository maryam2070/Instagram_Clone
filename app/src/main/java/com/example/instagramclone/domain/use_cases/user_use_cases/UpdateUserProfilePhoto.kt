package com.example.instagramclone.domain.use_cases.user_use_cases

import android.net.Uri
import com.example.instagramclone.domain.repository.StorageRepository
import javax.inject.Inject

class UpdateUserProfilePhoto @Inject constructor(
    private val storageRepo: StorageRepository
) {
    operator fun invoke(uri: Uri, path: String) =storageRepo.uploadImg(uri, path)
}