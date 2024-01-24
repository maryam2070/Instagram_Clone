package com.example.instagramclone.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.instagramclone.domain.repository.PreferencesDataStoreRepository
import javax.inject.Inject

class PreferencesDataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferencesDataStoreRepository {


    override suspend fun addStoryId(id: String, value: Boolean) {

        dataStore.edit { pref ->
            pref.set(booleanPreferencesKey(id),value)
            pref.get(booleanPreferencesKey(id))

        }
    }


    override suspend fun deleteStoryId(id: String) {
        dataStore.edit {pref->
            pref.remove(booleanPreferencesKey(id))
        }
    }

    override suspend fun getAllIds(ids: List<String>): List<String> {

        val data= mutableListOf<String>()
        dataStore.edit { pref ->
            for (id in ids) {
                val value = pref.get(booleanPreferencesKey(id))
                value?.let {
                    data.add(id)
                }
            }
        }
        return data
    }
}