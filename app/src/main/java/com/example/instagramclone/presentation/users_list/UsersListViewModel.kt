package com.example.instagramclone.presentation.users_list
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.domain.model.Friend
import com.example.instagramclone.domain.use_cases.post_use_cases.GetPostLikes
import com.example.instagramclone.domain.use_cases.user_use_cases.GetFollowers
import com.example.instagramclone.domain.use_cases.user_use_cases.GetFollowing
import com.example.instagramclone.utils.Constants
import com.example.instagramclone.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UsersListViewModel @Inject constructor(
    private val getFollowers: GetFollowers,
    private val getFollowing: GetFollowing,
    private val getLikes:GetPostLikes
) : ViewModel() {

    private val _uiState = mutableStateOf(
        UsersListListUiState()
    )
    val uiState: State<UsersListListUiState> = _uiState


    fun getFollowers(userId: String) {
        viewModelScope.launch {
            getFollowers.invoke(userId)
                .collect { response->
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
                                _uiState.value.copy(isLoading = (true))
                        }

                        is Response.Success -> {
                            response.data?.let {
                                    _uiState.value=_uiState.value.copy(users = it, isLoading = _uiState.value.isLoading.and(false))
                            }
                        }
                    }
                }
        }
    }
    fun getFollowing(userId: String) {
        viewModelScope.launch {
            getFollowing.invoke(userId)
                .collect {response->
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
                                _uiState.value.copy(isLoading = (true))
                        }

                        is Response.Success -> {
                            response.data?.let {
                                _uiState.value=_uiState.value.copy(users = it, isLoading = _uiState.value.isLoading.and(false))
                            }
                        }
                    }
                }
        }
    }
    fun getLikes(postId: String) {
        viewModelScope.launch {
            getLikes.invoke(postId)
                .collect {response->
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
                                _uiState.value.copy(isLoading = (true))
                        }

                        is Response.Success -> {
                            response.data?.let {
                                val users=it.map{like->
                                    Friend(like.userName,like.userId)
                                }
                                _uiState.value=_uiState.value.copy(users=users, isLoading = _uiState.value.isLoading.and(false))
                            }
                        }
                    }
                }
        }
    }
}

