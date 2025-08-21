package com.example.geyugoapp.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.geyugoapp.domain.task.models.Task
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmManager: AlarmManager
) {

    companion object {
        private const val TAG = "TaskNotificationManager"
        private const val NOTIFICATION_ADVANCE_MILLIS = 5 * 60 * 1000L
    }

    fun scheduleNotification(task: Task): String {
        val notificationId = task.notificationId ?: UUID.randomUUID().toString()

        val notificationTime = task.dateTime - NOTIFICATION_ADVANCE_MILLIS
        val currentTime = System.currentTimeMillis()

        if (notificationTime <= currentTime) {
            return notificationId
        }

        val intent = Intent(context, TaskNotificationReceiver::class.java).apply {
            putExtra("taskId", task.id)
            putExtra("taskName", task.name)
            putExtra("notificationId", notificationId)
            putExtra("userId", task.userId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                notificationTime,
                pendingIntent
            )

            Log.d(TAG, "✅ Successfully scheduled notification for task ${task.id}")
            Log.d(TAG, "   Will trigger at: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date(notificationTime))}")
            Log.d(TAG, "   In ${(notificationTime - currentTime) / 1000} seconds")
            Log.d(TAG, "=====================================")
        } catch (e: SecurityException) {
            Log.e(TAG, "❌ Failed to schedule notification for task ${task.id}", e)
            Log.e(TAG, "   This might be due to missing SCHEDULE_EXACT_ALARM permission")
        }

        return notificationId
    }

    fun cancelNotification(notificationId: String) {
        val intent = Intent(context, TaskNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId.hashCode(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        pendingIntent?.let {
            alarmManager.cancel(it)
            it.cancel()
        }
    }

    fun cancelAllNotifications(tasks: List<Task>) {
        tasks.forEach { task ->
            task.notificationId?.let { notificationId ->
                cancelNotification(notificationId)
            }
        }
    }

    fun rescheduleAllNotifications(tasks: List<Task>): List<Task> {
        return tasks.map { task ->
            val newNotificationId = scheduleNotification(task)
            task.copy(notificationId = newNotificationId)
        }
    }
}