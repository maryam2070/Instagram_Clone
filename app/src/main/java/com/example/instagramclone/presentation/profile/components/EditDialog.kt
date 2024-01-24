package com.example.instagramclone.presentation.profile.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.instagramclone.domain.model.User
import com.example.instagramclone.utils.getImageLink
import com.intuit.sdp.R

@Composable
fun EditDialog(
    user: User,
    updateUserData: (String, Map<String, Any>) -> Unit,
    onDismiss: () -> Unit,
    updateUserPhoto: (uri: Uri) -> Unit
) {

    val name = remember {
        mutableStateOf(user.name)
    }

    val bio = remember {
        mutableStateOf(user.bio)
    }
    val selectedUri = remember {
        mutableStateOf<Uri?>(null)
    }
    val singlePhotoPickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                uri?.let {
                    selectedUri.value = it
                }
            })

    Dialog(onDismissRequest = {
        onDismiss()
    })
    {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .clip(
                    RoundedCornerShape(dimensionResource(id = R.dimen._20sdp))
                )
        ) {

            Column(
                modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen._10sdp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = selectedUri.value?: getImageLink(user.imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen._60sdp))
                        .clip(CircleShape)

                        .clickable(enabled = true, onClick = {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }),
                    contentScale = ContentScale.Crop
                )
                OutlinedTextField(value = name.value, onValueChange = {
                    name.value = it
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(id = R.dimen._20sdp),
                        end = dimensionResource(id = R.dimen._20sdp),
                        bottom = dimensionResource(id = R.dimen._20sdp)
                    ), label = { Text(text = "Name") })

                OutlinedTextField(value = bio.value, onValueChange = {
                    bio.value = it
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(id = R.dimen._20sdp),
                        end = dimensionResource(id = R.dimen._20sdp),
                        bottom = dimensionResource(id = R.dimen._20sdp)
                    ), label = { Text(text = "Bio") })

                Button(
                    onClick = {
                        updateUserData(
                            user.id,
                            mapOf("name" to name.value, "bio" to bio.value)
                        )
                        selectedUri.value?.let {
                            updateUserPhoto(it)
                        }

                        onDismiss()
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(id = R.dimen._20sdp),
                            end = dimensionResource(id = R.dimen._20sdp),
                            bottom = dimensionResource(id = R.dimen._20sdp)
                        )
                        .clip(RoundedCornerShape(20.dp))

                ) {
                    Text(
                        "Save",
                        Modifier.padding(dimensionResource(id = R.dimen._5sdp))
                    )
                }
            }


        }

    }

}

