package com.example.instagramclone.data

import android.util.Log
import com.example.instagramclone.domain.model.Story
import com.example.instagramclone.domain.repository.StoryRepository
import com.example.instagramclone.utils.Constants
import com.example.instagramclone.utils.Constants.COLLECTION_NAME_STORIES
import com.example.instagramclone.utils.Constants.USER_ID_FIELD_NAME
import com.example.instagramclone.utils.Response
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class StoryRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : StoryRepository {
    override fun uploadStory(story: Story): Flow<Response<Boolean>> = callbackFlow {
        try {
            firestore.collection(COLLECTION_NAME_STORIES)
                .document(story.id)
                .set(story)
                .addOnSuccessListener {
                    trySend(Response.Success(true))
                }.addOnFailureListener {
                    trySend(Response.Error(it.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
                }
        } catch (e: Exception) {
            trySend(Response.Error(e.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
        }
        awaitClose()
    }

    override fun deleteStory(storyId: String): Flow<Response<Boolean>> = callbackFlow {
        try {
            firestore.collection(COLLECTION_NAME_STORIES)
                .document(storyId)
                .delete()
                .addOnSuccessListener {
                    trySend(Response.Success(true))
                }.addOnFailureListener {
                    trySend(Response.Error(it.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
                }
        } catch (e: Exception) {
            trySend(Response.Error(e.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
        }
        awaitClose()
    }

    override fun getStories(following: List<String>): Flow<Response<List<Story>>> = callbackFlow {
        if (following.isNotEmpty()) {
            trySend(Response.Loading(isLoading = true))
            val snapshotListener =
                firestore.collection(COLLECTION_NAME_STORIES).whereIn(USER_ID_FIELD_NAME, following)
                    .addSnapshotListener { snapshot, e ->
                        if (snapshot != null) {
                            val stories = snapshot.toObjects(Story::class.java)
                            checkForStoriesUploadDate(stories)

                            trySend(Response.Success(stories))

                        } else {
                            trySend(Response.Error(e?.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
                        }

                    }
            trySend(Response.Loading(isLoading = false))
            awaitClose {
                snapshotListener.remove()
            }
        } else {
            trySend(Response.Success(emptyList()))
        }
        awaitClose()
    }

    override fun checkForStoriesUploadDate(stories: MutableList<Story>): Unit {
        CoroutineScope(dispatcher).launch {
            val ids = mutableListOf<String>()
            val storiesRef = firestore.collection(COLLECTION_NAME_STORIES)

            for (story in stories) {
                if (
                    story.time.compareTo(Timestamp.now()) < 0 //24 hours
                ) {
                    ids.add(story.id)
                    stories.remove(story)
                }
                if (ids.isNotEmpty()) {
                    try {
                        firestore.runTransaction {
                            ids.forEach { id ->
                                it.delete(storiesRef.document(id))
                            }
                            null
                        }
                            .addOnSuccessListener {
                                Log.d("checkForStoriesUploadDate", "success")
                            }.addOnFailureListener {
                                Log.d(
                                    "checkForStoriesUploadDate",
                                    it.message ?: Constants.UNKNOWN_ERROR_OCCURRED
                                )
                            }

                    } catch (e: Exception) {
                        Log.d(
                            "checkForStoriesUploadDate",
                            e.message ?: Constants.UNKNOWN_ERROR_OCCURRED
                        )
                    }
                }
            }
        }
    }

}