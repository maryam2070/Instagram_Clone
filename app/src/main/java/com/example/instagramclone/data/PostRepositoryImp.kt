package com.example.instagramclone.data

import com.example.instagramclone.domain.model.Comment
import com.example.instagramclone.domain.model.Like
import com.example.instagramclone.domain.model.Post
import com.example.instagramclone.domain.repository.PostRepository
import com.example.instagramclone.utils.Constants
import com.example.instagramclone.utils.Constants.AUTHOR_ID_FIELD_NAME
import com.example.instagramclone.utils.Constants.AUTHOR_NAME_FIELD_NAME
import com.example.instagramclone.utils.Constants.COLLECTION_NAME_COMMENTS
import com.example.instagramclone.utils.Constants.COLLECTION_NAME_LIKES
import com.example.instagramclone.utils.Constants.COLLECTION_NAME_POSTS
import com.example.instagramclone.utils.Constants.COLLECTION_NAME_USERS
import com.example.instagramclone.utils.Constants.CONTENT_FIELD_NAME
import com.example.instagramclone.utils.Constants.ID_FIELD_NAME
import com.example.instagramclone.utils.Constants.TOTAL_POSTS_FIELD_NAME
import com.example.instagramclone.utils.Constants.USER_ID_FIELD_NAME
import com.example.instagramclone.utils.Constants.USER_NAME_FIELD_NAME
import com.example.instagramclone.utils.Response
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class PostRepositoryImp @Inject constructor(
    private val fireBaseFireStore: FirebaseFirestore
) : PostRepository {
    override fun getAllPosts(userId: String): Flow<Response<List<Post>>> = callbackFlow {
        trySend(Response.Loading(isLoading = true))
        val snapshotListener =
            fireBaseFireStore.collection(COLLECTION_NAME_POSTS).whereEqualTo(USER_ID_FIELD_NAME, userId)
                .addSnapshotListener { snapshot, e ->
                    if (snapshot != null) {
                        val postList = snapshot.toObjects(Post::class.java)
                        trySend( Response.Success(postList))
                    } else {
                        trySend(Response.Error(e?.message?: Constants.UNKNOWN_ERROR_OCCURRED))
                    }

                }
        trySend(Response.Loading(isLoading = false))

        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun uploadPost(
        postId:String,
        postDescription: String,
        userId: String,
        userName: String,
        notificationToken:String
    ): Flow<Response<Boolean>> = callbackFlow {
        try {
            val postRef = fireBaseFireStore.collection(COLLECTION_NAME_POSTS).document(postId)
            val userRef = fireBaseFireStore.collection(COLLECTION_NAME_USERS).document(userId)
            val post = Post(
                postId = postId,
                postDescription = postDescription,
                time = Timestamp.now(),
                userId = userId,
                userName = userName,
                notificationToken=notificationToken
            )

            fireBaseFireStore.runTransaction() { transaction ->
                transaction.set(postRef, post)
                transaction.update(userRef, TOTAL_POSTS_FIELD_NAME, FieldValue.increment(1))
                null
            }.addOnSuccessListener {
                trySend(Response.Success(true))
            }.addOnFailureListener {
                trySend(Response.Error(it.message?: Constants.UNKNOWN_ERROR_OCCURRED))
            }

        } catch (e: Exception) {
            trySend(Response.Error(e.message?: Constants.UNKNOWN_ERROR_OCCURRED))
        }
        trySend(Response.Loading(isLoading = false))
        awaitClose()
    }

    override fun getFeed(following: List<String>): Flow<Response<List<Post>>> = callbackFlow {

        if(following.isNotEmpty()) {
            trySend(Response.Loading(isLoading = true))
            val snapshotListener =
                fireBaseFireStore.collection(COLLECTION_NAME_POSTS)
                    .whereIn(USER_ID_FIELD_NAME, following)
                    .addSnapshotListener { snapshot, e ->
                        if (snapshot != null) {
                            val postList = snapshot.toObjects(Post::class.java)
                            trySend(Response.Success(data = postList))
                        } else {
                            trySend(Response.Error(e?.message ?: Constants.UNKNOWN_ERROR_OCCURRED))
                        }
                    }

            trySend(Response.Loading(isLoading = false))
            awaitClose {
                snapshotListener.remove()
            }
        }else{
            trySend(Response.Success(emptyList()))
        }
        awaitClose()
    }

    override fun updatePostData(
        postId: String,
        field: String,
        value: FieldValue
    ): Flow<Response<Boolean>> = callbackFlow {
        try {
            fireBaseFireStore.collection(COLLECTION_NAME_POSTS).document(postId).update(field, value)
                .addOnSuccessListener {
                    trySend(Response.Success(true))
                }.addOnFailureListener {

                    trySend(Response.Error(it.message?: Constants.UNKNOWN_ERROR_OCCURRED))
                }
        } catch (e: Exception) {
            trySend(Response.Error(e.message?: Constants.UNKNOWN_ERROR_OCCURRED))
        }
        trySend(Response.Loading(isLoading = false))
        awaitClose()
    }

    override fun getPost(postId: String): Flow<Response<Post>> = callbackFlow {
        trySend(Response.Loading(isLoading = true))
        val snapshotListener =
            fireBaseFireStore.collection(COLLECTION_NAME_POSTS).document(postId)
                .addSnapshotListener { snapshot, e ->
                    if (snapshot != null) {
                        val post = snapshot.toObject(Post::class.java)
                        trySend(Response.Success(data = post))
                    } else {
                        trySend(Response.Error(e?.message?: Constants.UNKNOWN_ERROR_OCCURRED))
                    }
                }
        trySend(Response.Loading(isLoading = false))

        awaitClose {
            snapshotListener.remove()
        }
    }
    override fun getPostComments(postId: String): Flow<Response<List<Comment>>> = callbackFlow {
        trySend(Response.Loading(isLoading = true))
        val snapshotListener =
            fireBaseFireStore.collection(COLLECTION_NAME_POSTS).document(postId)
                .collection(COLLECTION_NAME_COMMENTS)
                .addSnapshotListener { snapshot, e ->
                    if (snapshot != null) {
                        val comments = snapshot.toObjects(Comment::class.java)
                        trySend(Response.Success(data = comments))
                    } else {
                        trySend(Response.Error(e?.message?: Constants.UNKNOWN_ERROR_OCCURRED))
                    }
                }
        trySend(Response.Loading(isLoading = false))

        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun getPostLikes(postId: String): Flow<Response<List<Like>>> = callbackFlow{
        trySend(Response.Loading(isLoading = true))
        val snapshotListener =
            fireBaseFireStore.collection(COLLECTION_NAME_POSTS).document(postId)
                .collection(COLLECTION_NAME_LIKES)
                .addSnapshotListener { snapshot, e ->
                    if (snapshot != null) {
                        val likes = snapshot.toObjects(Like::class.java)
                        trySend(Response.Success(data = likes))
                    } else {
                        trySend(Response.Error(e?.message?: Constants.UNKNOWN_ERROR_OCCURRED))
                    }
                }
        trySend(Response.Loading(isLoading = false))

        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun deleteLike(userId: String,postId: String, likeId: String): Flow<Response<Boolean>> = callbackFlow {
        try {

            trySend(Response.Loading(isLoading = true))
            val likeRef = fireBaseFireStore.collection(COLLECTION_NAME_POSTS).document(postId)
                .collection(COLLECTION_NAME_LIKES).document(likeId)

            val postRef=fireBaseFireStore.collection(COLLECTION_NAME_POSTS).document(postId)

            fireBaseFireStore.runTransaction { transition->
                transition.delete(likeRef)
                transition.update(postRef,COLLECTION_NAME_LIKES,FieldValue.arrayRemove(userId))
                null
            }
                .addOnSuccessListener {
                    trySend(Response.Success(true))
                }.addOnFailureListener {

                    trySend(Response.Error(it.message?: Constants.UNKNOWN_ERROR_OCCURRED))
                }
        } catch (e: Exception) {
            trySend(Response.Error(e.message?: Constants.UNKNOWN_ERROR_OCCURRED))
        }
        trySend(Response.Loading(isLoading = false))
        awaitClose()
    }


    override fun addComment(
        postId: String,
        content: String,
        authorId: String,
        authorName: String
    ): Flow<Response<Boolean>> = callbackFlow {
        try {
            trySend(Response.Loading(isLoading = true))
            val doc = fireBaseFireStore.collection(COLLECTION_NAME_POSTS).document(postId)
                .collection(COLLECTION_NAME_COMMENTS).document()

            doc.set(
                mapOf(
                    ID_FIELD_NAME to doc.id,
                    CONTENT_FIELD_NAME to content,
                    AUTHOR_ID_FIELD_NAME to authorId,
                    AUTHOR_NAME_FIELD_NAME to authorName
                )
            )
                .addOnSuccessListener {
                    trySend(Response.Success(true))
                }.addOnFailureListener {

                    trySend(Response.Error(it.message?: Constants.UNKNOWN_ERROR_OCCURRED))
                }
        } catch (e: Exception) {
            trySend(Response.Error(e.message?: Constants.UNKNOWN_ERROR_OCCURRED))
        }
        trySend(Response.Loading(isLoading = false))
        awaitClose()
    }

    override fun addLike(
        postId: String,
        userId: String,
        userName: String
    ): Flow<Response<Boolean>> = callbackFlow {
        try {
            trySend(Response.Loading(isLoading = true))
            val likeRef = fireBaseFireStore.collection(COLLECTION_NAME_POSTS).document(postId)
                .collection(COLLECTION_NAME_LIKES).document(userId)

            val postRef=fireBaseFireStore.collection(COLLECTION_NAME_POSTS).document(postId)

            fireBaseFireStore.runTransaction { transition->
                transition.set(likeRef,
                    mapOf(
                        ID_FIELD_NAME to likeRef.id,
                        USER_ID_FIELD_NAME to userId,
                        USER_NAME_FIELD_NAME to userName
                    )
                )
                transition.update(postRef,COLLECTION_NAME_LIKES,FieldValue.arrayUnion(userId))
                null
            }
                .addOnSuccessListener {
                    trySend(Response.Success(true))
                }.addOnFailureListener {

                    trySend(Response.Error(it.message?: Constants.UNKNOWN_ERROR_OCCURRED))
                }
        } catch (e: Exception) {
            trySend(Response.Error(e.message?: Constants.UNKNOWN_ERROR_OCCURRED))
        }
        trySend(Response.Loading(isLoading = false))
        awaitClose()
    }

    override fun deletePost(postId: String,userId: String): Flow<Response<Boolean>> = callbackFlow {
        try {
            val postRef=fireBaseFireStore.collection(COLLECTION_NAME_POSTS).document(postId)
            val userRef=fireBaseFireStore.collection(COLLECTION_NAME_USERS).document(userId)

            fireBaseFireStore.runTransaction() { transaction ->

                transaction.delete(postRef)
                transaction.update(userRef,TOTAL_POSTS_FIELD_NAME,FieldValue.increment(-1))
                null
            }
                .addOnSuccessListener {
                    trySend(Response.Success(true))
                }.addOnFailureListener {
                    trySend(Response.Error(it.message?: Constants.UNKNOWN_ERROR_OCCURRED))
                }

        } catch (e: Exception) {
            trySend(Response.Error(e.message?: Constants.UNKNOWN_ERROR_OCCURRED))
        }
        trySend(Response.Loading(isLoading = false))
        awaitClose()
    }

    override fun deleteComment(postId: String, commentId: String): Flow<Response<Boolean>> = callbackFlow {
        try {
            fireBaseFireStore.collection(COLLECTION_NAME_POSTS)
                .document(postId)
                .collection(COLLECTION_NAME_COMMENTS)
                .document(commentId)
                .delete()
                .addOnSuccessListener {
                    trySend(Response.Success(true))
                }.addOnFailureListener {
                    trySend(Response.Error(it.message?: Constants.UNKNOWN_ERROR_OCCURRED))
                }
        } catch (e: Exception) {
            trySend(Response.Error(e.message?: Constants.UNKNOWN_ERROR_OCCURRED))
        }
        trySend(Response.Loading(isLoading = false))
        awaitClose()
    }
}