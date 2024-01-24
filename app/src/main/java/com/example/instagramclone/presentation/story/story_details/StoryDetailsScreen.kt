package com.example.instagramclone.presentation.story.story_details

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.instagramclone.domain.model.Story
import com.example.instagramclone.presentation.navigation.Screens
import com.example.instagramclone.presentation.story.story_details.components.DropDownMenu
import com.example.instagramclone.utils.getImageLink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun StoryDetailsScreen(story: Story, userId: String, navController: NavHostController) {

    val progress = remember {
        mutableFloatStateOf(0F)
    }

    val viewModel: StoryDetailsViewModel = hiltViewModel()
    if (viewModel.uiState.value.isDeleted) {
        Toast.makeText(LocalContext.current, "Story deleted successfully", Toast.LENGTH_SHORT)
            .show()
        navController.popBackStack()
        navController.navigate(Screens.FeedsScreen.route)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LinearProgressIndicator(
                progress = progress.floatValue,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .height(5.dp)
                    .fillMaxWidth(), trackColor = MaterialTheme.colorScheme.background
            )


            Row(
                Modifier
                    .padding(20.dp)
                    .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = getImageLink(story.userId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.size(5.dp))
                Text(story.userName, Modifier.padding(start = 20.dp))

                if (userId == story.userId) {
                    DropDownMenu {
                        viewModel.deleteStory(story.id)
                    }
                }
            }
            AsyncImage(
                model = getImageLink(story.id),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.8F),
                contentScale = ContentScale.Crop
            )
            Text(
                text = story.content,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                fontSize = 20.sp
            )
        }
    }

    CoroutineScope(Dispatchers.Main).launch {
        progress.floatValue = progress.floatValue + 0.005F
        if (progress.floatValue >= 1F) {
            navController.popBackStack()
            navController.navigate(Screens.FeedsScreen.route)
        }
    }

}