package com.example.geyugoapp.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.geyugoapp.MainActivity
import com.example.geyugoapp.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManager
) {
    
    companion object {
        private const val TAG = "NotificationService"
        private const val CHANNEL_ID = "task_notifications"
        private const val CHANNEL_NAME = "Task Reminders"
        private const val CHANNEL_DESCRIPTION = "Notifications for upcoming tasks"
    }
    
    init {
        createNotificationChannel()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "Creating notification channel...")
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
                setShowBadge(true)
            }
            
            notificationManager.createNotificationChannel(channel)
            Log.d(TAG, "✅ Notification channel created: $CHANNEL_ID")
        } else {
            Log.d(TAG, "Android version < O, no need to create notification channel")
        }
    }
    
    fun showTaskNotification(
        taskId: Long,
        taskName: String,
        notificationId: String
    ) {
        Log.d(TAG, "📱 === SHOWING NOTIFICATION ===")
        Log.d(TAG, "Task ID: $taskId")
        Log.d(TAG, "Task Name: $taskName")
        Log.d(TAG, "Notification ID: $notificationId")
        Log.d(TAG, "Channel ID: $CHANNEL_ID")
        
        try {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            
            val pendingIntent = PendingIntent.getActivity(
                context,
                taskId.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            Log.d(TAG, "Creating notification...")
            
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("Upcoming Task")
                .setContentText("$taskName starts in 5 minutes")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("Don't forget: $taskName starts in 5 minutes!")
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()
            
            val notificationIdInt = notificationId.hashCode()
            Log.d(TAG, "Notification hash code: $notificationIdInt")
            
            notificationManager.notify(notificationIdInt, notification)
            
            Log.d(TAG, "🎉 Notification sent successfully!")
            Log.d(TAG, "===============================")
            
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error showing notification", e)
            Log.e(TAG, "Exception: ${e.message}")
            e.printStackTrace()
        }
    }
}