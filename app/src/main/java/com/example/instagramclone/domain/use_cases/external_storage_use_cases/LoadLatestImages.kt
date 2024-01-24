package com.example.instagramclone.domain.use_cases.external_storage_use_cases

import com.example.instagramclone.domain.repository.ExternalStorageRepository
import javax.inject.Inject

class LoadLatestImages @Inject constructor(
    private val repository: ExternalStorageRepository
) {
    suspend operator fun invoke() = repository.loadLatestImagesFromExternalStorage()
}