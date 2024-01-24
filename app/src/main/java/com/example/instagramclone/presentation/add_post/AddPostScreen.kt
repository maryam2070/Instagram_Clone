package com.example.instagramclone.presentation.add_post

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
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
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.instagramclone.R
import com.example.instagramclone.presentation.add_post.components.AddPostCaption
import com.example.instagramclone.presentation.navigation.Screens
import com.intuit.sdp.R.dimen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AddPostScreen(navController: NavController) {


    val granted = remember {
        mutableStateOf(false)
    }


    val viewModel: AddPostViewModel = hiltViewModel()

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        granted.value = isGranted

    }


    CheckPermissions(launcher)

    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()


    if (viewModel.uiState.value.isUploaded == 2) {
        LaunchUploadedSnackBar(coroutineScope, snackBarHostState, navController)
    }

    if (viewModel.uiState.value.isError) {
        LaunchErrorSnackBar(coroutineScope, snackBarHostState, viewModel)
    }
    Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }) { padding ->
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
                viewModel.getPhotos()
                PermissionGrantedView(viewModel)
            } else {

                PermissionNotGranted(launcher)
            }
        }
    }
}

@Composable
private fun PermissionGrantedView(
    viewModel: AddPostViewModel
) {
    val borderWidth = remember {
        mutableIntStateOf(0)
    }
    Column {

        AddPostCaption(viewModel.userId.value, viewModel.userName.value) {
            viewModel.uploadPost(
                it,
                viewModel.userId.value,
                viewModel.userName.value,
                viewModel.uri.value
            )

        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize()
        ) {
            items(viewModel.uiState.value.photos.size) {
                borderWidth.intValue = 0
                viewModel.uri.value.let { uri ->
                    if (uri == viewModel.uiState.value.photos[it].contentUri) {
                        borderWidth.intValue = 4
                    }
                }
                Image(
                    painter = rememberAsyncImagePainter(viewModel.uiState.value.photos[it].contentUri),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .border(width = borderWidth.intValue.dp, color = Color.Gray)
                        .clickable {
                            viewModel.setUri(viewModel.uiState.value.photos[it].contentUri)
                        }
                )
            }
        }
    }
}

@Composable
private fun LaunchErrorSnackBar(
    coroutineScope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    viewModel: AddPostViewModel
) {
    LaunchedEffect(key1 = true, block = {

        coroutineScope.launch {
            snackBarHostState.showSnackbar(
                viewModel.uiState.value.errorMessage
            )
        }
    })
}

@Composable
private fun LaunchUploadedSnackBar(

    coroutineScope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    navController: NavController
) {
    LaunchedEffect(key1 = (true), block = {
        coroutineScope.launch {
            snackBarHostState.showSnackbar(
                "Post uploaded successfully"
            )
            navController.navigate(Screens.FeedsScreen.route)
        }
    })
}

@Composable
private fun CheckPermissions(launcher: ManagedActivityResultLauncher<String, Boolean>) {
    LaunchedEffect(key1 = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            launcher.launch(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
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
            painter = rememberAsyncImagePainter(R.drawable.permission_denied),
            contentDescription = "",
            modifier = Modifier
                .size(dimensionResource(id = dimen._200sdp))
        )

        Text(
            "Storage permission denied, you need this permission to upload photo in your posts," +
                    " please grant it.", Modifier.padding(dimensionResource(id = dimen._20sdp))
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
        ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    )
    context.startActivity(intent)
}

