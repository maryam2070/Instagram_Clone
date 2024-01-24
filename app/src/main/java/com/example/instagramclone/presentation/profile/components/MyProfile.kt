package com.example.instagramclone.presentation.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyProfile(
    displayName: String,
    bio: String
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        Text(text = displayName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = bio, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(5.dp))


    }

}