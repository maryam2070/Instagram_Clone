package com.example.instagramclone.presentation.signup


import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ehsanmsz.mszprogressindicator.progressindicator.BallZigZagProgressIndicator
import com.example.instagramclone.R
import com.example.instagramclone.presentation.navigation.Graphs
import com.example.instagramclone.presentation.navigation.Screens
import com.example.instagramclone.utils.Response

@Composable
fun SignUpScreen(navController: NavController) {

    val viewModel: SignUpViewModel = hiltViewModel()


    val configuration = LocalConfiguration.current
    val padding = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        0.dp
    } else {
        dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
    }


    when (val response = viewModel.signUp.value) {
        is Response.Error -> {
            viewModel.showCircularProgressIndicator.value = false
            Toast.makeText(
                LocalContext.current, response.message, Toast.LENGTH_LONG
            ).show()
        }

        is Response.Loading -> {
            viewModel.showCircularProgressIndicator.value = response.isLoading
        }

        is Response.Success -> {
            viewModel.showCircularProgressIndicator.value = false
            Toast.makeText(
                LocalContext.current,
                "Please check the inbox for provided email to verify the account",
                Toast.LENGTH_SHORT
            ).show()
            navController.navigate(Graphs.PAGES) {
                popUpTo(Screens.LoginScreen.route) {
                    inclusive = true
                }
            }


        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (viewModel.showCircularProgressIndicator.value) {
            BallZigZagProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp))
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                modifier = Modifier
                    .size(dimensionResource(id = com.intuit.sdp.R.dimen._150sdp))
                    .padding(vertical = padding)
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.linearGradient(
                                    listOf(
                                        Color(red = 108, green = 25, blue = 103),
                                        Color(red = 227, green = 157, blue = 209),
                                        Color(red = 184, green = 217, blue = 248),
                                    )
                                ), blendMode = BlendMode.SrcAtop
                            )
                        }
                    },
                contentDescription = stringResource(id = R.string.logo_description)
            )


            OutlinedTextField(value = viewModel.name.value,
                onValueChange = {
                    viewModel.name.value = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = padding),
                isError = viewModel.nameValidation.value?.isSuccess?.not() ?: false,
                supportingText = {
                    viewModel.nameValidation.value?.let {
                        if (!it.isSuccess) {
                            Text(
                                text = getString(LocalContext.current, it.message),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                },
                label = { Text(text = "Name") })

            OutlinedTextField(value = viewModel.email.value,
                onValueChange = {
                    viewModel.email.value = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = padding),
                isError = viewModel.emailValidation.value?.isSuccess?.not() ?: false,
                supportingText = {
                    viewModel.emailValidation.value?.let {
                        if (!it.isSuccess) {
                            Text(
                                text = getString(LocalContext.current, it.message),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                },
                label = { Text(text = "Email") })

            OutlinedTextField(
                value = viewModel.password.value,
                onValueChange = {
                    viewModel.password.value = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = padding),
                isError = viewModel.passwordValidation.value?.isSuccess?.not() ?: false,
                supportingText = {
                    viewModel.passwordValidation.value?.let {
                        if (!it.isSuccess) {
                            Text(
                                text = getString(LocalContext.current, it.message),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                },
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation()
            )
            OutlinedButton(
                onClick = {
                    viewModel.signUp()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = padding),
            ) {
                Text(
                    text = "Sign Up",
                    Modifier.padding(dimensionResource(id = com.intuit.sdp.R.dimen._8sdp))
                )

            }

        }
    }
}