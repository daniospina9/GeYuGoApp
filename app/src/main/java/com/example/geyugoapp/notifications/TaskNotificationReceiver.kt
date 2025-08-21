package com.example.geyugoapp.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.geyugoapp.domain.notifications.usecases.GetNotificationSettingsByUserId
import com.example.geyugoapp.domain.task.usecases.GetTasksByUserId
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TaskNotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationService: NotificationService

    @Inject
    lateinit var getTasksByUserId: GetTasksByUserId

    @Inject
    lateinit var getNotificationSettings: GetNotificationSettingsByUserId

    companion object {
        private const val TAG = "TaskNotificationReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {

        val taskId = intent.getLongExtra("taskId", -1L)
        val taskName = intent.getStringExtra("taskName") ?: ""
        val notificationId = intent.getStringExtra("notificationId") ?: ""
        val userId = intent.getLongExtra("userId", -1L)

        if (taskId == -1L || userId == -1L) {
            return
        }

        if (taskName.isEmpty() || notificationId.isEmpty()) {
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(TAG, "🔍 Checking if task still exists...")

                // Find the task
                val allTasks = getTasksByUserId(userId)
                Log.d(TAG, "Found ${allTasks.size} tasks for user $userId")

                val task = allTasks.find { it.id == taskId }

                if (task == null) {
                    return@launch
                }

                // Check if notifications are still enabled for this user

                val notificationSettings = getNotificationSettings(userId)

                if (notificationSettings?.areNotificationsEnabled != true) {
                    return@launch
                }

                // Show the notification
                notificationService.showTaskNotification(
                    taskId = taskId,
                    taskName = taskName,
                    notificationId = notificationId
                )

            } catch (e: Exception) {
                Log.e(TAG, "❌ Error processing notification for task $taskId", e)
                Log.e(TAG, "Exception details: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}