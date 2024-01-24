package com.example.instagramclone.presentation.users_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.instagramclone.presentation.navigation.Screens
import com.example.instagramclone.presentation.users_list.UsersListNavigation
import com.intuit.sdp.R


@Composable
fun Header(navigationType: UsersListNavigation, navController: NavHostController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = navigationType.name,
            modifier = Modifier
                .padding( dimensionResource(id = R.dimen._10sdp)),
            fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._18ssp).value.sp,
            style = TextStyle(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(red = 182, green = 217, blue = 247),
                        Color(red = 227, green = 161, blue = 207),
                        Color(red = 110, green = 26, blue = 103)
                    ),
                    tileMode = TileMode.Mirror
                ),
            )
        )
        if (navigationType == UsersListNavigation.FOLLOWING) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "",
                modifier = Modifier.clickable {
                    navController.navigate(Screens.AddNewFollowingScreen.route)
                }
                    .padding(end= dimensionResource(id = R.dimen._8sdp),top= dimensionResource(id = R.dimen._8sdp))
                    .size(dimensionResource(id = R.dimen._30sdp))
                    .align(Alignment.CenterVertically)
            )
        }
    }
}