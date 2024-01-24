package com.example.instagramclone.presentation.story.add_story

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ehsanmsz.mszprogressindicator.progressindicator.BallZigZagProgressIndicator
import com.example.instagramclone.domain.model.Story
import com.example.instagramclone.presentation.story.add_story.components.AddStoryView
import com.google.firebase.Timestamp
import com.intuit.sdp.R
import kotlinx.coroutines.launch
import java.util.UUID

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AddStoryScreen(navController: NavController) {

    val viewModel: AddStoryViewModel = hiltViewModel()

    val granted = remember {
        mutableStateOf(false)
    }
    val borderWidth = remember {
        mutableStateOf(0)
    }


    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            granted.value = true
        } else {
            granted.value = false
        }
    }
    LaunchedEffect(key1 = true) {
        launcher.launch(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

    }
    val snackbarHostState = remember { SnackbarHostState() }// this contains the `SnackbarHostState`
    val coroutineScope = rememberCoroutineScope()

    if (viewModel.uiState.value.isUploaded == 2) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                "Story Uploaded Successfully"
            )

            navController.navigateUp()
        }
    }
    if (viewModel.uiState.value.isError) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                viewModel.uiState.value.errorMessage
            )
        }
    }
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (viewModel.uiState.value.isLoading)
                BallZigZagProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                )


            if (granted.value) {
                Column {
                    AddStoryView(viewModel.userId.value, viewModel.userName.value) {
                        val id = UUID.randomUUID()
                        viewModel.uploadData(
                            viewModel.uri.value,
                            id.toString(),
                            Story(
                                id.toString(),
                                it,
                                viewModel.userId.value,
                                viewModel.userName.value,
                                Timestamp.now()
                            )
                        )

                    }
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(viewModel.pickedPhotos.value.size) {
                            borderWidth.value = 0
                            viewModel.uri.value.let { uri ->
                                if (uri == viewModel.pickedPhotos.value[it].contentUri) {
                                    borderWidth.value = 4
                                }
                            }
                            Image(
                                painter = rememberAsyncImagePainter(viewModel.pickedPhotos.value[it].contentUri),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .border(width = borderWidth.value.dp, color = Color.Gray)
                                    .clickable {
                                        viewModel.setUri(viewModel.pickedPhotos.value[it].contentUri)
                                    }
                            )
                        }
                    }
                }
            } else {

                PermissionNotGranted(launcher)
            }
        }
    }
}


@Composable
fun PermissionNotGranted(launcher: ManagedActivityResultLauncher<String, Boolean>) {
    val context = LocalContext.current
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(com.example.instagramclone.R.drawable.permission_denied),
            contentDescription = "",
            modifier = Modifier
                .size(dimensionResource(id = R.dimen._200sdp))
        )

        Text(
            "Storage permission denied, you need this permission to upload photo in your posts," +
                    " please grant it.", Modifier.padding(dimensionResource(id = R.dimen._20sdp))
        )
        OutlinedButton(onClick = {
            openAppSettings(context)
            launcher.launch(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

        }) {
            Text("Go to Settings")
        }
    }
}

fun openAppSettings(context: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    )
    context.startActivity(intent)
}
