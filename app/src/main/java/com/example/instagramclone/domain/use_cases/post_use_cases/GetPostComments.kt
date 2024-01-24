package com.example.instagramclone.domain.use_cases.post_use_cases

import com.example.instagramclone.domain.repository.PostRepository
import javax.inject.Inject

class GetPostComments  @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(postId: String) = repository.getPostComments(postId)
}