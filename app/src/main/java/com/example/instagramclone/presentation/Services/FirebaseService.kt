package com.example.instagramclone.presentation.Services


import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.instagramclone.R
import com.example.instagramclone.domain.use_cases.notification_use_cases.WriteNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class FirebaseService @Inject constructor(
) : FirebaseMessagingService() {

    private val CHANNEL_ID = "my_notification_channel"

    @Inject
    lateinit var writeNotification: WriteNotification


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)


        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random.nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val navigationIntent =
            Intent().apply {
                val type = message.data.get("type")?.toInt()
                action = Intent.ACTION_VIEW

                data = if (type == 1 || type == 2)
                    "app://example.instagramclone.com/postId=${message.data.get("postId")}".toUri()
                else {
                    "app://example.instagramclone.com/chatItem=${message.data.get("chatItem")}".toUri()
                }
            }


        val pendingIntent: PendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(navigationIntent)
            getPendingIntent(
                1,
                FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(
                message.data.get("title").toString()
            )
            .setContentText(
                message.data.get("body").toString()
            )
            .setSmallIcon(R.drawable.logo)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notificationManager.notify(notificationId, notification)
            }
        } else {

            notificationManager.notify(notificationId, notification)
        }

        if(message.data.get("type") == "1" ||message.data.get("type") =="2") {
            writeNotification.invoke(
                message.data.get("senderId").toString(),
                message.data.get("receiverId").toString(),
                message.data.get("title").toString(),
                message.data.get("body").toString(),
                message.data.get("postId").toString()
            )
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {

        val channelName = "ChannelFirebaseChat"
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
            description = "MY FIREBASE CHAT DESCRIPTION"
            enableLights(true)
            lightColor = Color.WHITE
        }
        notificationManager.createNotificationChannel(channel)
    }
}
