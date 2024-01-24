package com.example.instagramclone.domain.use_cases.story_use_case

import com.example.instagramclone.domain.model.Story
import com.example.instagramclone.domain.repository.StoryRepository
import javax.inject.Inject

class UploadStory @Inject constructor(private val repository: StoryRepository) {

    operator fun invoke(story: Story) = repository.uploadStory(story)
}
