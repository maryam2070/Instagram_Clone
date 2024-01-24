package com.example.instagramclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.os.LocaleListCompat
import androidx.navigation.compose.rememberNavController
import com.example.instagramclone.presentation.navigation.BottomNavigationMenu
import com.example.instagramclone.presentation.navigation.RootNavigation
import com.example.instagramclone.ui.theme.InstagramCloneTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("en-US")
        AppCompatDelegate.setApplicationLocales(appLocale)
        setContent {
            InstagramCloneTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background

                ) {
                    val navController = rememberNavController()
                    Scaffold(bottomBar = {
                        BottomNavigationMenu(
                            navController = navController
                        )
                    }) { padding ->
                        Column(modifier = Modifier.padding(bottom = padding.calculateBottomPadding())) {

                                RootNavigation(
                                    navController = navController
                                )
                        }
                    }
                }
            }
        }
    }
}

