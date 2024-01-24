package com.example.instagramclone.domain.repository

import com.example.instagramclone.domain.model.Story
import com.example.instagramclone.utils.Response
import kotlinx.coroutines.flow.Flow

interface StoryRepository {
    fun uploadStory(story: Story): Flow<Response<Boolean>>
    fun deleteStory(storyId: String): Flow<Response<Boolean>>
    fun getStories(following:List<String>):Flow<Response<List<Story>>>
    fun checkForStoriesUploadDate(stories: MutableList<Story>)
}