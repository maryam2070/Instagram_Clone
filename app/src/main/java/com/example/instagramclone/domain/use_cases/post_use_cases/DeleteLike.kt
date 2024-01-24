package com.example.instagramclone.domain.use_cases.post_use_cases

import com.example.instagramclone.domain.repository.PostRepository
import javax.inject.Inject

class DeleteLike  @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(userId:String,postId: String,likeId:String) =
        repository.deleteLike(userId,postId,likeId)
}