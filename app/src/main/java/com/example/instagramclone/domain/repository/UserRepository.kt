package com.example.instagramclone.domain.repository

import com.example.instagramclone.domain.model.Friend
import com.example.instagramclone.domain.model.User
import com.example.instagramclone.utils.Response
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserDetails(userId: String): Flow<Response<User>>
    fun getUsersWithNameQuery(query: String):Flow<Response<List<User>>>
    fun setUserDetails(
        userId: String, data: Map<String, Any>
    ): Flow<Response<Boolean>>

    fun updateUserDetails(
        userId: String,
        data: Map<String, Any>
    ): Flow<Response<Boolean>>

    fun getFollowers(userId: String): Flow<Response<List<Friend>>>
    fun getFollowing(userId: String): Flow<Response<List<Friend>>>

    fun addFollower(userId: String,userName:String, follower: Friend): Flow<Response<Boolean>>
    fun removeFollower(userId: String,follower: Friend): Flow<Response<Boolean>>

    fun checkIfFriendExist(userId:String,friendId:String): Flow<Response<Boolean>>
    fun getUsers(ids: List<String>): Flow<Response<List<User>>>

}