package com.example.instagramclone.domain.use_cases.story_use_case

import com.example.instagramclone.domain.repository.StoryRepository
import javax.inject.Inject


class DeleteStory @Inject constructor(private val repository: StoryRepository) {

     operator fun invoke(storyId:String)= repository.deleteStory(storyId)

}

