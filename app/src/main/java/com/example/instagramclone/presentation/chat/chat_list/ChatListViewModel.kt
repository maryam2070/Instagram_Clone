package com.example.instagramclone.presentation.chat.chat_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.domain.model.ChatForUser
import com.example.instagramclone.domain.use_cases.auth_use_cases.GetUser
import com.example.instagramclone.domain.use_cases.chat_use_cases.GetChats
import com.example.instagramclone.utils.Constants
import com.example.instagramclone.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatListViewModel  @Inject constructor(
    private val getChats: GetChats,
    private val getUser: GetUser
) : ViewModel() {
    private val _userId = mutableStateOf("")
    val userId = _userId

    private val _uiState = mutableStateOf(
        ChatListUiState()
    )
    val uiState: State<ChatListUiState> = _uiState

    init {
        getUser.invoke()?.let {
            _userId.value = it.uid
            getChats(it.uid)
        }
    }

    fun filterChats(query:String){
        val chats=_uiState.value.chats
        _uiState.value=_uiState.value.copy(filteredChats =
            chats.filter {
                it.friendName.contains(query)
            }
        )
    }

    fun getChats(userId: String) {
        viewModelScope.launch {
            getChats.invoke(userId).collect { response ->
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

                            val chats=it.sortedBy {
                                it.lastMessageTime
                            }
                            _uiState.value = _uiState.value.copy(
                                isLoading = _uiState.value.isLoading.and(false),
                                chats = chats,
                                filteredChats = chats
                            )

                        }
                    }
                }
            }
        }
    }
}
