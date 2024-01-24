package com.example.instagramclone.presentation.feed

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.presentation.post_details.PostViewModel
import com.example.instagramclone.domain.use_cases.auth_use_cases.GetUser
import com.example.instagramclone.domain.use_cases.img_use_cases.DeleteImg
import com.example.instagramclone.domain.use_cases.notification_use_cases.SendNotification
import com.example.instagramclone.domain.use_cases.post_use_cases.AddComment
import com.example.instagramclone.domain.use_cases.post_use_cases.AddLike
import com.example.instagramclone.domain.use_cases.post_use_cases.DeleteComment
import com.example.instagramclone.domain.use_cases.post_use_cases.DeleteLike
import com.example.instagramclone.domain.use_cases.post_use_cases.DeletePost
import com.example.instagramclone.domain.use_cases.post_use_cases.GetFeed
import com.example.instagramclone.domain.use_cases.post_use_cases.GetPost
import com.example.instagramclone.domain.use_cases.post_use_cases.GetPostComments
import com.example.instagramclone.domain.use_cases.post_use_cases.GetPostLikes
import com.example.instagramclone.domain.use_cases.story_use_case.AddStoryIdToDataStore
import com.example.instagramclone.domain.use_cases.story_use_case.GetAllIdsFromDataStore
import com.example.instagramclone.domain.use_cases.story_use_case.GetStories
import com.example.instagramclone.domain.use_cases.user_use_cases.GetFollowing
import com.example.instagramclone.utils.Constants
import com.example.instagramclone.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedsViewModel @Inject constructor(
    private val getFeed: GetFeed,
    private val getFollowing: GetFollowing,
    private val getStories: GetStories,
    private val getAllIdsFromDataStore: GetAllIdsFromDataStore,
    private val addStoryIdToDataStore: AddStoryIdToDataStore,
    sendNotification: SendNotification,
    getUser: GetUser,
    deleteLike: DeleteLike,
    getPostComments: GetPostComments,
    addComment: AddComment,
    addLike: AddLike,
    getPostLikes: GetPostLikes,
    deletePost: DeletePost,
    deleteComment: DeleteComment,
    getPost: GetPost,
    deleteImg: DeleteImg
) : PostViewModel(
    getPostComments,
    addComment,
    addLike,
    getPostLikes,
    deleteLike,
    deleteComment,
    deletePost,
    getUser,
    getPost,
    deleteImg,
    sendNotification
) {
    private val _userId = mutableStateOf("")
    override val userId = _userId

    private val _userName = mutableStateOf("")
    override val userName = _userName

    init {
        getUser.invoke()?.let {
            _userId.value = it.uid
            _userName.value = it.displayName.toString()
            getFollowing(_userId.value)
        }
    }

    private val _uiState = mutableStateOf<FeedUiState>(
        FeedUiState()
    )
    val uiState: State<FeedUiState> = _uiState


    fun getFollowing(userId: String) {
        viewModelScope.launch {
            getFollowing.invoke(userId).collect { response ->

                when (response) {
                    is Response.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = _uiState.value.isLoading.and(false),
                            isError = true,
                            errorMessage = response.message ?: Constants.UNKNOWN_ERROR_OCCURRED
                        )
                    }

                    is Response.Loading -> {
                        _uiState.value =
                            _uiState.value.copy(isLoading = _uiState.value.isLoading.and(true))
                    }
                    is Response.Success -> {
                        response.data?.let {
                            val ids = it.map {
                                it.id
                            }

                            getFeed(ids.plus(userId))
                            getStories(ids.plus(userId))
                        }
                    }
                }
            }
        }
    }

    private fun getFeed(following: List<String>) {
        viewModelScope.launch {
            getFeed.invoke(following).collect {
                when (it) {
                    is Response.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = _uiState.value.isLoading.and(false),
                            isError = true,
                            errorMessage = it.message ?: Constants.UNKNOWN_ERROR_OCCURRED
                        )
                    }

                    is Response.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = _uiState.value.isLoading.and(true)
                        )
                    }

                    is Response.Success -> {

                        _uiState.value = _uiState.value.copy(
                            isLoading = _uiState.value.isLoading.and(false),
                            feed = it.data?.sortedByDescending {
                                it.time
                            } ?: emptyList()
                        )
                    }
                }

            }
        }
    }

    private fun getStories(following: List<String>) {
        viewModelScope.launch {
            getStories.invoke(following).collect { response ->
                when (response) {
                    is Response.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = _uiState.value.isLoading.and(false),
                            isError = true,
                            errorMessage = response.message ?: Constants.UNKNOWN_ERROR_OCCURRED
                        )
                    }

                    is Response.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is Response.Success -> {
                        response.data?.let {
                            val openedStories = getAllStoriesFromDataStore(it.map {
                                it.id
                            })

                            _uiState.value = _uiState.value.copy(
                                isLoading = _uiState.value.isLoading.and(false),
                                stories = (it.map {
                                    it.copy(isOpened = openedStories.contains(it.id))
                                }.sortedByDescending {
                                    it.time
                                }) ?: emptyList())

                        }

                    }
                }
            }
        }
    }

    fun addStoryToDataStore(id: String, value: Boolean) = viewModelScope.launch {
        addStoryIdToDataStore.invoke(id, value)
    }

    private suspend fun getAllStoriesFromDataStore(ids: List<String>) =
        getAllIdsFromDataStore.invoke(ids)


}

