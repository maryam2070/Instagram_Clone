package com.example.instagramclone.presentation.image_view

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.instagramclone.R
import com.example.instagramclone.presentation.image_view.components.DropDownMenu
import com.example.instagramclone.utils.getImageLink
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable


@Composable
fun ImageViewScreen(imagePath: String?) {

    val viewModel:ImageViewViewModel= hiltViewModel()

    val painter = painterResource(id = R.drawable.logo)
    val zoomState = rememberZoomState(contentSize = painter.intrinsicSize)

    Column (modifier = Modifier.fillMaxSize()){
       val bitamp= BitmapFactory.decodeResource(
            LocalContext.current.resources,
            R.drawable.logo)

        DropDownMenu {
            viewModel.saveImage(bitamp)
        }

        imagePath?.let{
            AsyncImage(
                model = getImageLink(imagePath),
                contentDescription = "Zoomable image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .zoomable(zoomState),
            )
        }
    }
}

