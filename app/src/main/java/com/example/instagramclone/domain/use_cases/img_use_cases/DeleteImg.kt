package com.example.instagramclone.domain.use_cases.img_use_cases

import com.example.instagramclone.domain.repository.StorageRepository
import javax.inject.Inject

class DeleteImg @Inject constructor(
    private val repository: StorageRepository
) {
    operator fun invoke(path:String) = repository.deleteImg(path)
}