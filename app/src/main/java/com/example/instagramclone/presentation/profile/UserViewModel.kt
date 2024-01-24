package com.example.instagramclone.presentation.profile

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.domain.model.Friend
import com.example.instagramclone.domain.model.ProfileNavigationType
import com.example.instagramclone.domain.use_cases.auth_use_cases.FirebaseSignOut
import com.example.instagramclone.domain.use_cases.auth_use_cases.GetUser
import com.example.instagramclone.domain.use_cases.img_use_cases.DeleteImg
import com.example.instagramclone.domain.use_cases.notification_use_cases.SendNotification
import com.example.instagramclone.domain.use_cases.post_use_cases.AddComment
import com.example.instagramclone.domain.use_cases.post_use_cases.AddLike
import com.example.instagramclone.domain.use_cases.post_use_cases.DeleteComment
import com.example.instagramclone.domain.use_cases.post_use_cases.DeleteLike
import com.example.instagramclone.domain.use_cases.post_use_cases.DeletePost
import com.example.instagramclone.domain.use_cases.post_use_cases.GetAllPosts
import com.example.instagramclone.domain.use_cases.post_use_cases.GetPost
import com.example.instagramclone.domain.use_cases.post_use_cases.GetPostComments
import com.example.instagramclone.domain.use_cases.post_use_cases.GetPostLikes
import com.example.instagramclone.domain.use_cases.user_use_cases.AddFollower
import com.example.instagramclone.domain.use_cases.user_use_cases.CheckIfFriendExist
import com.example.instagramclone.domain.use_cases.user_use_cases.GetUserDetails
import com.example.instagramclone.domain.use_cases.user_use_cases.RemoveFollower
import com.example.instagramclone.domain.use_cases.user_use_cases.UpdateUserDetails
import com.example.instagramclone.domain.use_cases.user_use_cases.UpdateUserProfilePhoto
import com.example.instagramclone.presentation.post_details.PostViewModel
import com.example.instagramclone.utils.Constants
import com.example.instagramclone.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserDetails: GetUserDetails,
    private val updateUserProfilePhoto: UpdateUserProfilePhoto,
    private val addFollower: AddFollower,
    private val getAllPosts: GetAllPosts,
    private val updateUserDetails: UpdateUserDetails,
    private val removeFollower: RemoveFollower,
    private val checkIfFriendExist: CheckIfFriendExist,
    private val getUser: GetUser,
    private val signOut: FirebaseSignOut,
    deleteLike: DeleteLike,
    getPostComments: GetPostComments,
    addComment: AddComment,
    addLike: AddLike,
    getPostLikes: GetPostLikes,
    deletePost: DeletePost,
    deleteComment: DeleteComment,
    getPost: GetPost,
    deleteImg: DeleteImg,
    sendNotification: SendNotification,

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

    private val _uiState = mutableStateOf(
        ProfileUiState()
    )
    val uiState: State<ProfileUiState> = _uiState


    private val _showEditDialog = mutableStateOf(false)
    val showEditDialog = _showEditDialog

    fun setShowEditDialog(value: Boolean) {
        _showEditDialog.value = value
    }


    init {
        getUser.invoke()?.let {
            _userId.value = it.uid
            _userName.value = it.displayName.toString()
        }
    }

    fun getPosts(userId: String) = viewModelScope.launch {
        getAllPosts.invoke(userId).collect {

            when (it) {
                is Response.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = _uiState.value.isLoading.and(false),
                        isError = true,
                        errorMessage = it.message ?: Constants.UNKNOWN_ERROR_OCCURRED
                    )
                }

                is Response.Loading -> {
                    _uiState.value =
                        _uiState.value.copy(isLoading = _uiState.value.isLoading.and(true))
                }

                is Response.Success -> {
                    _uiState.value =
                        _uiState.value.copy(isLoading = _uiState.value.isLoading.and(false),posts = it.data ?: emptyList())

                }
            }

        }
    }

    fun updateUserProfilePhoto(uri: Uri) {
        viewModelScope.launch {
            updateUserProfilePhoto.invoke(uri, _userId.value).collect {
                when (it) {
                    is Response.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = it.message ?: Constants.UNKNOWN_ERROR_OCCURRED
                        )
                    }

                    is Response.Loading -> {
                        _uiState.value =
                            _uiState.value.copy(isLoading = true)
                    }

                    is Response.Success -> {
                        _uiState.value = _uiState.value.copy(isLoading = _uiState.value.isLoading.and(false))

                    }
                }
            }
        }
    }

    fun getUserInfo(userId: String) {
        viewModelScope.launch {
            getUserDetails.invoke(userId).collect {
                when (it) {
                    is Response.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = _uiState.value.isLoading.and(false),
                            isError = true,
                            errorMessage = it.message ?: Constants.UNKNOWN_ERROR_OCCURRED
                        )
                    }

                    is Response.Loading -> {
                        _uiState.value =
                            _uiState.value.copy(isLoading = _uiState.value.isLoading.and(true))
                    }

                    is Response.Success -> {
                        _uiState.value =
                            _uiState.value.copy(isLoading = _uiState.value.isLoading.and(false), user = it.data!!)

                    }
                }
            }
        }
    }

    fun updateUserData(
        userId: String, data: Map<String, Any>
    ) {
        viewModelScope.launch {
            updateUserDetails.invoke(
                userId, data
            ).collect {
                when (it) {
                    is Response.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = it.message ?: Constants.UNKNOWN_ERROR_OCCURRED
                        )
                    }

                    is Response.Loading -> {
                        _uiState.value =
                            _uiState.value.copy(isLoading = true)
                    }

                    is Response.Success -> {
                        _uiState.value = _uiState.value.copy(isLoading = _uiState.value.isLoading.and(false))

                    }
                }
            }
        }

    }

    fun addFollower(userId: String, userName: String, follower: Friend) {
        viewModelScope.launch {
            addFollower.invoke(userId, userName, follower).collect {
                when (it) {
                    is Response.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = _uiState.value.isLoading.and(false),
                            isError = true,
                            errorMessage = it.message ?: Constants.UNKNOWN_ERROR_OCCURRED
                        )
                    }

                    is Response.Loading -> {
                        _uiState.value =
                            _uiState.value.copy(isLoading = _uiState.value.isLoading.and(true))
                    }

                    is Response.Success -> {
                        _uiState.value =
                            _uiState.value.copy(isLoading = _uiState.value.isLoading.and(false))

                    }
                }
            }
        }
    }

    fun removeFollower(userId: String, follower: Friend) {
        viewModelScope.launch {
            removeFollower.invoke(userId, follower).collect {
                when (it) {
                    is Response.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = _uiState.value.isLoading.and(false),
                            isError = true,
                            errorMessage = it.message ?: Constants.UNKNOWN_ERROR_OCCURRED
                        )
                    }

                    is Response.Loading -> {
                        _uiState.value =
                            _uiState.value.copy(isLoading = _uiState.value.isLoading.and(true))
                    }

                    is Response.Success -> {
                        _uiState.value =
                            _uiState.value.copy(isLoading = _uiState.value.isLoading.and(false))

                    }
                }
            }
        }
    }

    fun checkIfFollowerExist(userId: String, friendId: String) {
        viewModelScope.launch {
            if (_userId.value == friendId) {
                _uiState.value =
                    _uiState.value.copy(navigationType = ProfileNavigationType.USER_PROFILE)
            } else {
                checkIfFriendExist.invoke(userId, friendId).collect {

                    when (it) {
                        is Response.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = _uiState.value.isLoading.and(false),
                                isError = true,
                                errorMessage = it.message ?: Constants.UNKNOWN_ERROR_OCCURRED
                            )
                        }

                        is Response.Loading -> {
                            _uiState.value =
                                _uiState.value.copy(isLoading = _uiState.value.isLoading.and(true))
                        }

                        is Response.Success -> {
                            if (it.data == true) {
                                _uiState.value =
                                    _uiState.value.copy(
                                        isLoading = false,
                                        navigationType = ProfileNavigationType.FRIEND_PROFILE
                                    )
                            } else {
                                _uiState.value =
                                    _uiState.value.copy(
                                        isLoading = false,
                                        navigationType = ProfileNavigationType.NOT_FRIEND_PROFILE
                                    )
                            }
                        }
                    }

                }
            }
        }
    }



    fun signOut() = viewModelScope.launch {
        signOut.invoke().collect {

            when (it) {
                is Response.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = _uiState.value.isLoading.and(false),
                        isError = true,
                        errorMessage = it.message ?: Constants.UNKNOWN_ERROR_OCCURRED
                    )
                }

                is Response.Loading -> {
                    _uiState.value =
                        _uiState.value.copy(isLoading = _uiState.value.isLoading.and(true))
                }

                is Response.Success -> {
                    _uiState.value =
                        _uiState.value.copy(isLoading = _uiState.value.isLoading.and(false), signedOut = true)

                }
            }

        }
    }

}

