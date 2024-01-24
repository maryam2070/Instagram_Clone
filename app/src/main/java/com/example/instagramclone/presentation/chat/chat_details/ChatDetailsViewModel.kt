package com.example.instagramclone.presentation.chat.chat_details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.domain.model.ChatForUser
import com.example.instagramclone.domain.model.NotificationData
import com.example.instagramclone.domain.model.PushNotification
import com.example.instagramclone.domain.use_cases.auth_use_cases.GetUser
import com.example.instagramclone.domain.use_cases.chat_use_cases.GetChat
import com.example.instagramclone.domain.use_cases.chat_use_cases.SendMessage
import com.example.instagramclone.domain.use_cases.notification_use_cases.SendNotification
import com.example.instagramclone.utils.Constants
import com.example.instagramclone.utils.Response
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatDetailsViewModel @Inject constructor(
    private val sendMessage: SendMessage,
    private val getChat: GetChat,
    private val sendNotification: SendNotification,
    private val getUser: GetUser
) : ViewModel() {

    private val _userId = mutableStateOf("")
    val userId = _userId

    private val _userName = mutableStateOf("")
    val userName = _userName

    private val _userToken = mutableStateOf("")
    val userToken = _userToken

    private val _uiState = mutableStateOf(
        ChatDetailsUiState()
    )
    val uiState: State<ChatDetailsUiState> = _uiState

    init {
        getUser.invoke()?.let {
            _userId.value = it.uid
            _userName.value = it.displayName.toString()
        }

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                _userToken.value = task.result?:""
            }

    }


    private fun sendNotification(token: String, title: String, message: String, chat:ChatForUser, receiverId:String) =
        viewModelScope.launch {
            sendNotification(
                PushNotification(
                    to = token,
                    NotificationData(title, message,3,"", Gson().toJson(chat),_userId.value,receiverId)
                )
            ).collect {

            }
        }


    fun sendMessage(
        senderId: String,
        senderName: String,
        senderNotificationToken: String,
        receiverId: String,
        receiverName: String,
        receiverNotificationToken: String,
        message: String,
        chatItem:ChatForUser
    ) {
        viewModelScope.launch {
            sendMessage.invoke(
                senderId,
                senderName,
                senderNotificationToken,
                receiverId,
                receiverName,
                receiverNotificationToken,
                message,
                chatItem.id
            ).collect { response ->
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
                        response.data?.let{

                            _uiState.value = _uiState.value.copy(
                                isLoading = _uiState.value.isLoading.and(false),
                            )
                            sendNotification(receiverNotificationToken,"${senderName} messaged you",message,chatItem,receiverId)

                        }
                    }
                }
            }
        }
    }

    fun getChat(chatId: String) = viewModelScope.launch {
        getChat.invoke(chatId).collect { response ->
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
                    response.data?.let { messages ->

                        _uiState.value = _uiState.value.copy(
                            isLoading = _uiState.value.isLoading.and(false),
                            messages = messages.sortedByDescending {
                                it.createdAt
                            }
                        )
                    }
                }
            }

        }
    }
}

