package com.example.instagramclone.domain.use_cases.story_use_case

import com.example.instagramclone.domain.repository.PreferencesDataStoreRepository
import javax.inject.Inject

class GetAllIdsFromDataStore @Inject constructor(
    private val repository: PreferencesDataStoreRepository
) {

    suspend operator fun invoke(ids:List<String>) = repository.getAllIds(ids)

}
