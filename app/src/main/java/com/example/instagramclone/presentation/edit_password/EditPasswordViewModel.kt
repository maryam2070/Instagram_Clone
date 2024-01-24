package com.example.instagramclone.presentation.edit_password

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.domain.use_cases.auth_use_cases.ChangePassword
import com.example.instagramclone.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditPasswordViewModel  @Inject constructor(
    private val changePassword: ChangePassword
) : ViewModel() {
    private val _response = mutableStateOf<Response<Boolean>>(Response.Loading(false))
    val response: State<Response<Boolean>> = _response

    fun changePassword(email: String,
               password: String) {
        viewModelScope.launch {
            _response.value= Response.Loading(true)
            changePassword.invoke(email, password )
                .collect {
                    _response.value = it
                }
        }
    }
}