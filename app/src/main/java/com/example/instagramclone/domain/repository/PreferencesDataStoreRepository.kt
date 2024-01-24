package com.example.instagramclone.domain.repository


interface PreferencesDataStoreRepository {
    suspend fun addStoryId(id:String,value:Boolean)
    suspend fun deleteStoryId(id: String)
    suspend fun getAllIds(ids: List<String>): List<String>
}