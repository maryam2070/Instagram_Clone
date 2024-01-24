package com.example.instagramclone.domain.use_cases.story_use_case

import com.example.instagramclone.domain.repository.PreferencesDataStoreRepository
import javax.inject.Inject

class DeleteStoryIdFromDataStore  @Inject constructor(
    private val repository: PreferencesDataStoreRepository
) {

    suspend operator fun invoke(id:String) = repository.deleteStoryId(id)

}
