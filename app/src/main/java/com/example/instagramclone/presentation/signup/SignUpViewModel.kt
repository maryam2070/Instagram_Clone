package com.example.instagramclone.presentation.signup

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.domain.use_cases.auth_use_cases.FirebaseSignUp
import com.example.instagramclone.domain.use_cases.img_use_cases.UploadImg
import com.example.instagramclone.utils.Response
import com.example.instagramclone.utils.validator.BaseValidator
import com.example.instagramclone.utils.validator.EmailValidator
import com.example.instagramclone.utils.validator.EmptyValidator
import com.example.instagramclone.utils.validator.PasswordValidator
import com.example.instagramclone.utils.validator.ValidateResult
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val firebaseSignUp: FirebaseSignUp,
    private val uploadImg: UploadImg
) : ViewModel() {

    private val _signUpState = mutableStateOf<Response<FirebaseUser>>(Response.Loading(false))
    val signUp: State<Response<FirebaseUser>> = _signUpState

    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val name = mutableStateOf("")

    val emailValidation = mutableStateOf<ValidateResult?>(null)
    val nameValidation = mutableStateOf<ValidateResult?>(null)
    val passwordValidation = mutableStateOf<ValidateResult?>(null)

    val showCircularProgressIndicator = mutableStateOf(false)


    fun signUp() = viewModelScope.launch {

        emailValidation.value = validateEmail()
        nameValidation.value = validateName()
        passwordValidation.value = validatePassword()

        if (emailValidation.value!!.isSuccess && passwordValidation.value!!.isSuccess && nameValidation.value!!.isSuccess) {
            _signUpState.value = Response.Loading(true)
            firebaseSignUp.invoke(
                email = email.value.trim(),
                password = password.value.trim(),
                username = name.value.trim()
            ).collect {
                _signUpState.value = it
                if (it is Response.Success) {
                    uploadImg.invoke(
                        Uri.parse("android.resource://com.example.instagramclone/drawable/cirle_avatar"),
                        it.data!!.uid
                    ).collect {
                    }
                }
            }
        }
    }

    private fun validateEmail() =
        BaseValidator.validate(EmptyValidator(email.value), EmailValidator(email.value.trim()))

    private fun validatePassword() =
        BaseValidator.validate(EmptyValidator(password.value), PasswordValidator(password.value))

    private fun validateName() = EmptyValidator(name.value).validate()

}