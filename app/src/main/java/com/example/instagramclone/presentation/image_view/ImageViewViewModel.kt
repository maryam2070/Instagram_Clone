package com.example.instagramclone.presentation.image_view

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.domain.use_cases.post_use_cases.SaveImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ImageViewViewModel @Inject constructor(
    private val saveImage: SaveImage
):ViewModel(){
    fun saveImage(bitmap: Bitmap){
        viewModelScope.launch {
          saveImage.invoke(bitmap)
        }
    }
}