package com.example.instagramclone.domain.use_cases.post_use_cases

import com.example.instagramclone.domain.repository.PostRepository
import javax.inject.Inject

class UploadPost @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(
        postId:String,
        postDescription: String,
        userId: String,
        userName: String,
        notificationToken:String
    ) = repository.uploadPost(
        postId,
        postDescription = postDescription,
        userId = userId,
        userName = userName,
        notificationToken
    )
}