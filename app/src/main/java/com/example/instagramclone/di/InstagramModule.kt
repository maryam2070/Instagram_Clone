package com.example.instagramclone.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.instagramclone.data.AuthRepositoryImpl
import com.example.instagramclone.data.ChatRepositoryImpl
import com.example.instagramclone.data.ExternalStorageRepositoryImpl
import com.example.instagramclone.data.NotificationApi
import com.example.instagramclone.data.NotificationRepositoryImpl
import com.example.instagramclone.data.PostRepositoryImp
import com.example.instagramclone.data.PreferencesDataStoreRepositoryImpl
import com.example.instagramclone.data.SaveImageRepositoryImpl
import com.example.instagramclone.data.StorageRepositoryImpl
import com.example.instagramclone.data.StoryRepositoryImpl
import com.example.instagramclone.data.UserRepositoryImp
import com.example.instagramclone.domain.repository.AuthRepository
import com.example.instagramclone.domain.repository.ChatRepository
import com.example.instagramclone.domain.repository.ExternalStorageRepository
import com.example.instagramclone.domain.repository.NotificationRepository
import com.example.instagramclone.domain.repository.PostRepository
import com.example.instagramclone.domain.repository.PreferencesDataStoreRepository
import com.example.instagramclone.domain.repository.SaveImageRepository
import com.example.instagramclone.domain.repository.StorageRepository
import com.example.instagramclone.domain.repository.StoryRepository
import com.example.instagramclone.domain.repository.UserRepository
import com.example.instagramclone.domain.use_cases.auth_use_cases.*
import com.example.instagramclone.utils.Constants
import com.example.instagramclone.utils.Constants.BASE_URL
import com.example.instagramclone.utils.Constants.NOTIFICATION_BASE_URL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object InstagramModule {

    @Provides
    @Singleton
    fun provideApiService(): NotificationApi {
        return Retrofit.Builder()
            .baseUrl(NOTIFICATION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NotificationApi::class.java)
    }

    @Singleton
    @Provides
    fun providesFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun providesFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance(BASE_URL)
    }

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = {
                appContext.preferencesDataStoreFile(Constants.STORY_PREFERENCES)
            }
        )

    @Provides
    @Singleton
    fun provideSaveImageRepository(@ApplicationContext appContext: Context): SaveImageRepository =
        SaveImageRepositoryImpl(appContext)


    @Provides
    @Singleton
    fun provideExternalStorageRepository(@ApplicationContext appContext: Context): ExternalStorageRepository =
        ExternalStorageRepositoryImpl(appContext=appContext)

    @Singleton
    @Provides
    fun providePreferencesDataStoreRepository(dataStore: DataStore<Preferences>): PreferencesDataStoreRepository {
        return PreferencesDataStoreRepositoryImpl(dataStore)
    }

    @Singleton
    @Provides
    fun providesAuthRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthRepository {
        return AuthRepositoryImpl(auth, firestore)
    }

    @Singleton
    @Provides
    fun providesStorageRepository(
        storage: FirebaseStorage
    ): StorageRepository = StorageRepositoryImpl(storage)

    @Singleton
    @Provides
    fun provideUserRepository(firestore: FirebaseFirestore,
                              auth: FirebaseAuth): UserRepository {
        return UserRepositoryImp(firestore,auth)
    }

    @Singleton
    @Provides
    fun provideChatRepository(firestore: FirebaseFirestore): ChatRepository {
        return ChatRepositoryImpl(firestore)
    }


    @Singleton
    @Provides
    fun providePostRepository(firestore: FirebaseFirestore): PostRepository {
        return PostRepositoryImp(fireBaseFireStore = firestore)
    }

    @Singleton
    @Provides
    fun provideStoryRepository(firestore: FirebaseFirestore): StoryRepository {
        return StoryRepositoryImpl(firestore)
    }
    @Singleton
    @Provides
    fun provideNotificationRepository(
        firestore: FirebaseFirestore,
        api: NotificationApi
    ): NotificationRepository {
        return NotificationRepositoryImpl(firestore, api)
    }

}