package com.example.instagramclone.presentation.story.story_details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.domain.use_cases.img_use_cases.DeleteImg
import com.example.instagramclone.domain.use_cases.story_use_case.DeleteStory
import com.example.instagramclone.utils.Constants
import com.example.instagramclone.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryDetailsViewModel @Inject constructor(
    private val deleteStory: DeleteStory,
    private val deleteImg: DeleteImg
) : ViewModel() {


    private val _uiState = mutableStateOf(
        StoryDetailsUiState()
    )
    val uiState = _uiState


    fun deleteStory(storyId: String) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true)
        merge(deleteStory.invoke(storyId),deleteImg.invoke(storyId))
        .collect { response ->
            when (response) {
                is Response.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = _uiState.value.isLoading.and(false),
                        isError = true,
                        errorMessage = response.message ?: Constants.UNKNOWN_ERROR_OCCURRED
                    )
                }

                is Response.Loading -> {
                    _uiState.value =
                        _uiState.value.copy(isLoading = _uiState.value.isLoading.and(true))
                }

                is Response.Success -> {
                    _uiState.value =
                                _uiState.value.copy(isDeleted=true,isLoading = _uiState.value.isLoading.and(false))

                }
            }
        }
    }
}
