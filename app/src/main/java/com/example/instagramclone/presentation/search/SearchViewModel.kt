package com.example.instagramclone.presentation.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.domain.model.User
import com.example.instagramclone.domain.use_cases.user_use_cases.GetUsersWithNameQuery
import com.example.instagramclone.utils.Constants
import com.example.instagramclone.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel@Inject constructor(
    private val getUsersWithNameQuery: GetUsersWithNameQuery
): ViewModel() {


    private val _uiState = mutableStateOf(
        SearchUiState()
    )
    val uiState: State<SearchUiState> = _uiState

    fun getUsersWithNameQuery(query:String){
        viewModelScope.launch {
            getUsersWithNameQuery.invoke(query).collect{response->
                when (response) {
                    is Response.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = response.message ?: Constants.UNKNOWN_ERROR_OCCURRED
                        )
                    }

                    is Response.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is Response.Success -> {
                            response.data?.let {
                                _uiState.value = _uiState.value.copy(
                                    isLoading = false,
                                    users = it
                                )
                            }
                    }
                }
            }
        }
    }

}

