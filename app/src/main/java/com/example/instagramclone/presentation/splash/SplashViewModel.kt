package com.example.instagramclone.presentation.splash

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.instagramclone.domain.use_cases.auth_use_cases.IsUserAuthenticated
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isUserAuthenticatedUseCase: IsUserAuthenticated
) : ViewModel() {

    var isUserAuthenticated: MutableState<Boolean> = mutableStateOf(isUserAuthenticatedUseCase.invoke())

}