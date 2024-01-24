package com.example.instagramclone.presentation.notifications

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.domain.model.Notification
import com.example.instagramclone.domain.use_cases.auth_use_cases.GetUser
import com.example.instagramclone.domain.use_cases.notification_use_cases.GetNotifications
import com.example.instagramclone.utils.Constants
import com.example.instagramclone.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val getNotifications: GetNotifications,
    private val getUser: GetUser
) :ViewModel() {

    private val _userId = mutableStateOf("")
    val userId = _userId


    private val _uiState = mutableStateOf(
        NotificationsUiState()
    )
    val uiState: State<NotificationsUiState> = _uiState

    init {
        getUser.invoke()?.let {
            _userId.value = it.uid

            getNotifications(it.uid)

        }
    }

    fun getNotifications(userId: String) = viewModelScope.launch {
        getNotifications.invoke(userId).collect { response ->
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
                    response.data?.let { notifications ->

                        _uiState.value = _uiState.value.copy(
                            isLoading = _uiState.value.isLoading.and(false),
                            notifications = notifications
                        )
                    }

                }
            }
        }
    }
}

