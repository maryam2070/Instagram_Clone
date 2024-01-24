package com.example.instagramclone.presentation.post_details

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.domain.model.Comment
import com.example.instagramclone.domain.model.Like
import com.example.instagramclone.domain.model.NotificationData
import com.example.instagramclone.domain.model.Post
import com.example.instagramclone.domain.model.PushNotification
import com.example.instagramclone.domain.use_cases.auth_use_cases.GetUser
import com.example.instagramclone.domain.use_cases.img_use_cases.DeleteImg
import com.example.instagramclone.domain.use_cases.notification_use_cases.SendNotification
import com.example.instagramclone.domain.use_cases.post_use_cases.AddComment
import com.example.instagramclone.domain.use_cases.post_use_cases.AddLike
import com.example.instagramclone.domain.use_cases.post_use_cases.DeleteComment
import com.example.instagramclone.domain.use_cases.post_use_cases.DeleteLike
import com.example.instagramclone.domain.use_cases.post_use_cases.DeletePost
import com.example.instagramclone.domain.use_cases.post_use_cases.GetPost
import com.example.instagramclone.domain.use_cases.post_use_cases.GetPostComments
import com.example.instagramclone.domain.use_cases.post_use_cases.GetPostLikes
import com.example.instagramclone.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
open class PostViewModel @Inject constructor(
    private val getPostComments: GetPostComments,
    private val addComment: AddComment,
    private val addLike: AddLike,
    private val getPostLikes: GetPostLikes,
    private val deleteLike: DeleteLike,
    private val deleteComment: DeleteComment,
    private val deletePost: DeletePost,
    private val getUser: GetUser,
    private val getPost: GetPost,
    private val deleteImg: DeleteImg,
    private val sendNotification: SendNotification
) : ViewModel() {
    private val _userId = mutableStateOf("")
    open val userId = _userId

    private val _userName = mutableStateOf("")
    open val userName = _userName

    private val _postsData = mutableStateOf<Response<List<Post>>>(
        Response.Loading(true)
    )
    open val postsData: State<Response<List<Post>>> = _postsData


    private val _postData = mutableStateOf<Response<Post>>(
        Response.Loading(true)
    )
    open val postData: State<Response<Post>> = _postData


    private val _commentsData = mutableStateOf<Response<List<Comment>>>(
        Response.Loading(true)
    )
    open val commentsData: State<Response<List<Comment>>> = _commentsData


    private val _likesData = mutableStateOf<Response<List<Like>>>(
        Response.Loading(true)
    )
    open val likesData: State<Response<List<Like>>> = _likesData


    open val showCircularProgressIndicator = mutableStateOf(false)


    init {
        getUser.invoke()?.let {
            _userId.value = it.uid
            _userName.value = it.displayName.toString()
        }
    }

    fun getPost(postId: String) = viewModelScope.launch {
        getPost.invoke(postId).collect {
            _postData.value = it
        }
    }

    fun getPostComments(postId: String) {
        viewModelScope.launch {
            getPostComments.invoke(postId)
                .collect {
                    _commentsData.value = it
                }
        }
    }

    fun addComment(
        postId: String,
        sendNotification: Boolean,
        content: String,
        authorId: String,
        authorName: String,
        notificationToken: String,
        postContent: String,
        postAuthorId: String
    ) {
        viewModelScope.launch {
            addComment.invoke(postId, content, authorId, authorName)
                .collect {
                    if (it is Response.Success && sendNotification) {
                            sendNotification(
                                notificationToken,
                                "$authorName commented on your post",
                                postContent,
                                postId,
                                2,
                                postAuthorId
                            )
                    }
                }
        }
    }

    fun addLike(
        postId: String,
        sendNotification: Boolean,
        userId: String,
        userName: String,
        notificationToken: String,
        postContent: String,
        postAuthorId: String
    ) {
        viewModelScope.launch {
            addLike.invoke(postId, userId, userName)
                .collect {
                    //_commentsData.value=it
                    Log.d("Add Like", (it.data ?: it.message).toString())
                    if (it is Response.Success && sendNotification) {
                        sendNotification(
                            notificationToken,
                            "$userName liked your post",
                            postContent,
                            postId,
                            1,
                            postAuthorId
                        )
                    }
                }
        }
    }

    fun deleteLike(userId: String, postId: String, likeId: String) {
        viewModelScope.launch {
            deleteLike.invoke(userId, postId, likeId)
                .collect {
                    //_commentsData.value=it
                }
        }
    }

    fun deleteComment(postId: String, commentId: String) {
        viewModelScope.launch {
            deleteComment.invoke(postId, commentId)
                .collect {
                    //_commentsData.value=it
                }
        }
    }

    fun deletePost(postId: String, userId: String) {
        viewModelScope.launch {
            merge(
                deleteImg.invoke(postId),
                deletePost.invoke(postId, userId)
            ).collect {
                //_commentsData.value=it
            }
        }
    }

    fun getPostLikes(postId: String) {
        viewModelScope.launch {
            getPostLikes.invoke(postId)
                .collect {
                    _likesData.value = it
                }
        }
    }

    private fun sendNotification(
        token: String,
        title: String,
        message: String,
        postId: String,
        type: Int,
        receiverId: String
    ) =
        viewModelScope.launch {
            sendNotification(
                PushNotification(
                    to = token,
                    NotificationData(
                        title, message, type, postId, "", _userId.value, receiverId
                    )
                )
            ).collect {

            }
        }

}