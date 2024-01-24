package com.example.instagramclone.presentation.splash

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.instagramclone.R
import com.example.instagramclone.presentation.navigation.Graphs
import com.example.instagramclone.presentation.navigation.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SplashScreen(navController: NavController) {

    val viewModel: SplashViewModel = hiltViewModel()
    CoroutineScope(Dispatchers.Main).launch {

        viewModel.isUserAuthenticated.value.also {
            if (it) {
                navController.navigate(Graphs.PAGES)
                {
                    popUpTo(Graphs.AUTHENTICATION) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            } else {
                navController.navigate(Screens.LoginScreen.route) {
                    popUpTo(Screens.SplashScreen.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {

        Icon(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize(.5F)
                .graphicsLayer(alpha = 0.99f)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(brush = Brush.linearGradient(listOf(
                            Color(red = 108, green = 25, blue = 103),
                            Color(red = 227, green = 157, blue = 209),
                            Color(red = 184, green = 217, blue = 248),
                        )), blendMode = BlendMode.SrcAtop)
                    }
                }
        )

    }

}
