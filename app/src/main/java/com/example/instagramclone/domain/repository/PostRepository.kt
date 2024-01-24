package com.example.instagramclone.domain.repository

import com.example.instagramclone.domain.model.Comment
import com.example.instagramclone.domain.model.Like
import com.example.instagramclone.domain.model.Post
import com.example.instagramclone.utils.Response
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.flow.Flow

interface PostRepository {


    fun uploadPost(
        postId:String,
        postDescription: String,
        userId: String,
        userName: String,
        notificationToken:String
    ): Flow<Response<Boolean>>

    //posts
    fun getAllPosts(userId:String) : Flow<Response<List<Post>>>

    fun getFeed(following:List<String>): Flow<Response<List<Post>>>

    fun updatePostData(postId:String, field: String, value: FieldValue): Flow<Response<Boolean>>

    fun getPost(postId: String): Flow<Response<Post>>

    fun deletePost(postId: String, userId: String): Flow<Response<Boolean>>

    //comments
    fun addComment(postId: String,content:String,authorId:String,authorName:String): Flow<Response<Boolean>>

    fun getPostComments(postId:String):Flow<Response<List<Comment>>>

    fun deleteComment(postId:String,commentId: String): Flow<Response<Boolean>>

    //likes
    fun addLike(postId: String,userId:String,userName:String): Flow<Response<Boolean>>

    fun getPostLikes(postId:String):Flow<Response<List<Like>>>

    fun deleteLike(userId: String,postId:String,likeId: String): Flow<Response<Boolean>>

}