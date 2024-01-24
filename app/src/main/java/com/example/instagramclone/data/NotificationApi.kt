package com.example.instagramclone.data

import com.example.instagramclone.domain.model.PushNotification
import com.example.instagramclone.utils.Constants.CONTENT_TYPE
import com.example.instagramclone.utils.Constants.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

fun interface NotificationApi {

    @Headers("Authorization: key=$SERVER_KEY","Content-type:$CONTENT_TYPE")
    @POST("/fcm/send")
    fun postNotification(
        @Body notification: PushNotification
    ): Call<ResponseBody>
}