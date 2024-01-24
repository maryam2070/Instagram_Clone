package com.example.instagramclone.presentation.story.add_story

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.domain.model.PickedImage
import com.example.instagramclone.domain.model.Story
import com.example.instagramclone.domain.use_cases.auth_use_cases.GetUser
import com.example.instagramclone.domain.use_cases.external_storage_use_cases.LoadLatestImages
import com.example.instagramclone.domain.use_cases.img_use_cases.UploadImg
import com.example.instagramclone.domain.use_cases.story_use_case.UploadStory
import com.example.instagramclone.utils.Constants
import com.example.instagramclone.utils.Response
import com.example.instagramclone.utils.validator.EmptyUriValidation
import com.example.instagramclone.utils.validator.ValidateResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val uploadImg: UploadImg,
    private val uploadStory: UploadStory,
    private val getUser: GetUser,
    private val loadLatestImages: LoadLatestImages
) : ViewModel() {

    val pickedPhotos = mutableStateOf<List<PickedImage>>(emptyList())

    private val _userId = mutableStateOf("")
    val userId = _userId

    private val _userName = mutableStateOf("")
    val userName = _userName


    private val uriValidation = mutableStateOf<ValidateResult?>(null)


    private val _uiState = mutableStateOf<AddStoryUiState>(
        AddStoryUiState()
    )
    val uiState = _uiState

    private val _uri = mutableStateOf<Uri?>(null)
    val uri = _uri

    fun setUri(uri: Uri) {
        _uri.value = uri
    }

    init {
        viewModelScope.launch {
            getUser.invoke()?.let {
                _userId.value = it.uid
                _userName.value = it.displayName.toString()
            }

            pickedPhotos.value = loadLatestImages()
        }
    }

    private val _response = mutableStateOf<Response<Boolean>>(
        Response.Loading(false)
    )
    val response: State<Response<Boolean>> = _response


    fun uploadData(
        uri: Uri?, path: String,
        story: Story
    ) = viewModelScope.launch {

        uriValidation.value = validateUri()

        if (uriValidation.value!!.isSuccess) {
            _uiState.value = _uiState.value.copy(isLoading = true)

            merge(uploadImg.invoke(uri!!, path), uploadStory.invoke(story))

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
                            response.data?.let {
                                val isUploaded = _uiState.value.isUploaded
                                _uiState.value = _uiState.value.copy(isUploaded = isUploaded + 1)
                                if (_uiState.value.isUploaded == 2)
                                    _uiState.value =
                                        _uiState.value.copy(
                                            isLoading = _uiState.value.isLoading.and(
                                                false
                                            )
                                        )
                            }
                        }
                    }
                }
        } else {
            _uiState.value = _uiState.value.copy(
                isError = true,
                errorMessage = "Please Select photo"
            )
        }
    }


    private fun validateUri(): ValidateResult = EmptyUriValidation(_uri.value).validate()
}

