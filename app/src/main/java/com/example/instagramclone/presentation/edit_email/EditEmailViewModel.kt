package com.example.instagramclone.presentation.edit_email

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.domain.use_cases.auth_use_cases.ChangeEmail
import com.example.instagramclone.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditEmailViewModel @Inject constructor(
    private val changeEmail: ChangeEmail
) :ViewModel() {
    private val _response = mutableStateOf<Response<Boolean>>(Response.Loading(false))
    val response: State<Response<Boolean>> = _response

    fun changeEmail(curEmail: String,
                    newEmail: String,
                    curPassword: String) {
        viewModelScope.launch {
            _response.value=Response.Loading(true)
            changeEmail.invoke(curEmail,  newEmail,curPassword)
                .collect {
                    _response.value = it
                }
        }

    }
}