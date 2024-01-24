package com.example.instagramclone.domain.use_cases.story_use_case

import com.example.instagramclone.domain.repository.StoryRepository
import javax.inject.Inject

class GetStories @Inject constructor(private val repository: StoryRepository) {

    operator fun invoke(following:List<String>) = repository.getStories(following)
}
