package com.example.instagramclone.presentation.edit_email

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.instagramclone.utils.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEmailScreen() {


    val showCircularProgressIndicator= remember {
        mutableStateOf(false)
    }
    val viewModel: EditEmailViewModel = hiltViewModel()
    when (val response = viewModel.response.value) {
        is Response.Error -> {
            showCircularProgressIndicator.value=false
            Toast.makeText(
                LocalContext.current,
                "There Is Error " + response.message,
                Toast.LENGTH_LONG
            ).show()
        }

        is Response.Loading -> {
            showCircularProgressIndicator.value =response.isLoading
        }

        is Response.Success -> {
            showCircularProgressIndicator.value=false
                response.data?.let {
                    Toast.makeText(
                        LocalContext.current,
                    "Email Updated Successfully " + response.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize(),contentAlignment= Alignment.Center) {
        if(showCircularProgressIndicator.value)
            CircularProgressIndicator(modifier = Modifier.size(100.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(
                    rememberScrollState(),
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val email = remember {
                mutableStateOf("")
            }
            val newEmail = remember {
                mutableStateOf("")
        }
            val password = remember {
                mutableStateOf("")
            }
            Spacer(modifier = Modifier.size(100.dp))

            Text(text = "Edit Email", fontSize = 25.sp)
            Spacer(modifier = Modifier.size(80.dp))

            OutlinedTextField(value = email.value, onValueChange = {
                email.value = it
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
                label = { Text(text = "Current Email") })

            OutlinedTextField(value = newEmail.value, onValueChange = {
                newEmail.value = it
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
                label = { Text(text = "New Email") })

            OutlinedTextField(
                value = password.value, onValueChange = {
                    password.value = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation()
            )

            OutlinedButton(
                onClick = {
                    viewModel.changeEmail(email.value.trim(),newEmail.value.trim(),password.value.trim())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 50.dp)
            ) {
                Text(text = "Change Email", Modifier.padding(5.dp))
            }
        }
    }
}