package com.devansab.begnn.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.devansab.begnn.R
import com.devansab.begnn.data.entities.Message
import com.devansab.begnn.views.activities.UserExternalActionActivity

class MyNotificationManager {


    companion object {
        const val MESSAGE_NOTIFICATION_CHANNEL = "Message notification"
        const val MESSAGE_NOTIFICATION_CHANNEL_DESC =
            "Display notification for the message received."
        const val MESSAGE_NOTIFICATION_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH
        fun showMessageNotification(message: Message) {
            val intent = Intent(MainApplication.instance, UserExternalActionActivity::class.java)
                .apply {
                    this.putExtra(
                        UserExternalActionActivity.TYPE,
                        UserExternalActionActivity.TYPE_NEW_MESSAGE_NOTIFICATION
                    )
                    this.putExtra(
                        UserExternalActionActivity.NEW_MESSAGE_USER_NAME,
                        message.userName
                    )
                    this.putExtra(
                        UserExternalActionActivity.NEW_MESSAGE_PERSON_TYPE,
                        message.isAnonymous
                    )
                    this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            val pendingIntent = PendingIntent.getActivity(
                MainApplication.instance,
                0, intent, 0
            )
            val builder =
                NotificationCompat.Builder(MainApplication.instance, MESSAGE_NOTIFICATION_CHANNEL)
                    .setSmallIcon(R.drawable.app_logo_white)
                    .setContentTitle("New message from: " + message.userName)
                    .setContentText(message.text)
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(message.text)
                    )
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(false)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)

            with(
                NotificationManagerCompat.from(MainApplication.instance)
            ) {
                notify(System.currentTimeMillis().toInt(), builder.build())
            }
        }

        fun createNotificationChannel(channelId: String, description: String, importance: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId, channelId, importance).apply {
                    this.description = description
                }
                val notificationManager: NotificationManager =
                    MainApplication.instance.getSystemService(Context.NOTIFICATION_SERVICE)
                            as NotificationManager

                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}