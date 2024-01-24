package com.example.instagramclone.domain.use_cases.post_use_cases

import com.example.instagramclone.domain.repository.PostRepository
import javax.inject.Inject

class GetFeed @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(
        following: List<String>
    ) = repository.getFeed(following)
}