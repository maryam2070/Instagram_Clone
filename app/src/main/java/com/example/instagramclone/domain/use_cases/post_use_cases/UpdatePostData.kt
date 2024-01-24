package com.example.instagramclone.domain.use_cases.post_use_cases

import com.example.instagramclone.domain.repository.PostRepository
import com.google.firebase.firestore.FieldValue
import javax.inject.Inject

class UpdatePostData @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(postId: String,field: String, value: FieldValue) = repository.updatePostData(postId, field,value)
}