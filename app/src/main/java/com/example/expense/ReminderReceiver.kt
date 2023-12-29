package com.example.expense

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class ReminderReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        showMorningNotification(context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showMorningNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = context.getString(R.string.notification_id)
        val appName = context.getString(R.string.app_name)
        val channel = NotificationChannel(channelId, appName, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(appName)
            .setContentText("Не забудьте внести інформацію про витрати!")
            .setSmallIcon(R.drawable.help)

        notificationManager.notify(123, notificationBuilder.build())
    }
}