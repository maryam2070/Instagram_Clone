package com.example.instagramclone.domain.use_cases.post_use_cases

import com.example.instagramclone.domain.repository.PostRepository
import javax.inject.Inject

class AddComment @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(postId: String,
                        content: String,
                        authorId: String,
                        authorName: String) =
        repository.addComment(postId,content, authorId, authorName)
}