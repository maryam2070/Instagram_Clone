package com.example.instagramclone.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.domain.use_cases.auth_use_cases.FirebaseSignIn
import com.example.instagramclone.utils.Response
import com.example.instagramclone.utils.validator.BaseValidator
import com.example.instagramclone.utils.validator.EmailValidator
import com.example.instagramclone.utils.validator.EmptyValidator
import com.example.instagramclone.utils.validator.ValidateResult
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseSignIn: FirebaseSignIn,

    ) : ViewModel() {

    private val _signInState = mutableStateOf<Response<FirebaseUser>>(Response.Loading(false))
    val signIn: State<Response<FirebaseUser>> = _signInState

    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val emailValidation = mutableStateOf<ValidateResult?>(null)
    val passwordValidation = mutableStateOf<ValidateResult?>(null)

    val showCircularProgressIndicator = mutableStateOf(false)


    fun signIn() = viewModelScope.launch {

        emailValidation.value = validateEmail()
        passwordValidation.value = validatePassword()

        if (emailValidation.value!!.isSuccess && passwordValidation.value!!.isSuccess) {
            firebaseSignIn.invoke(email = email.value.trim(), password = password.value.trim())
                .collect {
                    _signInState.value = it
                }
        }
    }


    private fun validateEmail() =
        BaseValidator.validate(EmptyValidator(email.value), EmailValidator(email.value.trim()))

    private fun validatePassword() = EmptyValidator(password.value).validate()

}