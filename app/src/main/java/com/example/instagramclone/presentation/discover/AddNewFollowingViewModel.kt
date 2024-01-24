package com.example.instagramclone.presentation.discover

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.domain.model.Friend
import com.example.instagramclone.domain.model.User
import com.example.instagramclone.domain.use_cases.auth_use_cases.GetUser
import com.example.instagramclone.domain.use_cases.user_use_cases.AddFollower
import com.example.instagramclone.domain.use_cases.user_use_cases.GetFollowing
import com.example.instagramclone.domain.use_cases.user_use_cases.GetUsers
import com.example.instagramclone.utils.Constants
import com.example.instagramclone.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddNewFollowingViewModel @Inject constructor(
    private val getUsers: GetUsers,
    private val addFollower: AddFollower,
    private val getUser: GetUser,
    private val getFollowing: GetFollowing,
):ViewModel() {

    private val _users= mutableStateOf<List<User>>(emptyList())
    val users=_users

    private val _userId = mutableStateOf("")
     val userId = _userId

    private val _userName = mutableStateOf("")
    val userName = _userName


    private val _uiState = mutableStateOf<AddNewFollowingUiState>(
        AddNewFollowingUiState()
    )
    val uiState: State<AddNewFollowingUiState> = _uiState

    init {
        getUser.invoke()?.let {
            _userId.value = it.uid
            _userName.value = it.displayName.toString()

            getFollowing(_userId.value)
        }
    }

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
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is Response.Success -> {
                        response.data?.let {
                            val ids = it.map {
                                it.id
                            }
                            getUsers(ids.plus(userId))
                        }
                    }
                }
            }
        }
    }

    private fun getUsers(ids: List<String>) =viewModelScope.launch {
        getUsers.invoke(ids).collect{ response ->

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

                    _uiState.value =
                        _uiState.value.copy(isLoading = _uiState.value.isLoading.and(false))
                    response.data?.let{
                        _users.value=it
                    }
                }
            }
        }
    }
    fun addFollow(userId:String,userName:String,follower: Friend)=viewModelScope.launch {
        addFollower.invoke(userId, userName, follower).collect { response ->

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
                        _uiState.value =
                            _uiState.value.copy(isLoading = _uiState.value.isLoading.and(false))
                    }
                }
            }
        }
    }
}


