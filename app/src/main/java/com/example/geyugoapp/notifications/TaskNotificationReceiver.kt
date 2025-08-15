package com.example.geyugoapp.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.geyugoapp.domain.notifications.usecases.GetNotificationSettingsByUserIdUseCase
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
    lateinit var getNotificationSettings: GetNotificationSettingsByUserIdUseCase
    
    companion object {
        private const val TAG = "TaskNotificationReceiver"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "🔔 === NOTIFICATION RECEIVER TRIGGERED ===")
        Log.d(TAG, "Timestamp: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date())}")
        
        val taskId = intent.getLongExtra("taskId", -1L)
        val taskName = intent.getStringExtra("taskName") ?: ""
        val notificationId = intent.getStringExtra("notificationId") ?: ""
        val userId = intent.getLongExtra("userId", -1L)
        
        Log.d(TAG, "Task ID: $taskId")
        Log.d(TAG, "Task Name: $taskName")
        Log.d(TAG, "Notification ID: $notificationId")
        Log.d(TAG, "User ID: $userId")
        
        if (taskId == -1L || userId == -1L) {
            Log.e(TAG, "❌ Invalid task ID or user ID received")
            Log.e(TAG, "   taskId: $taskId, userId: $userId")
            return
        }
        
        if (taskName.isEmpty() || notificationId.isEmpty()) {
            Log.e(TAG, "❌ Missing task name or notification ID")
            Log.e(TAG, "   taskName: '$taskName', notificationId: '$notificationId'")
            return
        }
        
        Log.d(TAG, "✅ All parameters valid, proceeding with checks...")
        
        // Use coroutine scope for async operations
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(TAG, "🔍 Checking if task still exists...")
                
                // Find the task 
                val allTasks = getTasksByUserId(userId)
                Log.d(TAG, "Found ${allTasks.size} tasks for user $userId")
                
                val task = allTasks.find { it.id == taskId }
                
                if (task == null) {
                    Log.w(TAG, "❌ Task $taskId no longer exists, skipping notification")
                    Log.w(TAG, "   Available task IDs: ${allTasks.map { it.id }}")
                    return@launch
                }
                
                Log.d(TAG, "✅ Task found: ${task.name}")
                
                // Check if notifications are still enabled for this user
                Log.d(TAG, "🔍 Checking notification settings...")
                val notificationSettings = getNotificationSettings(userId)
                Log.d(TAG, "Notification settings: $notificationSettings")
                
                if (notificationSettings?.areNotificationsEnabled != true) {
                    Log.w(TAG, "❌ Notifications disabled for user $userId, skipping")
                    Log.w(TAG, "   Settings enabled: ${notificationSettings?.areNotificationsEnabled}")
                    return@launch
                }
                
                Log.d(TAG, "✅ Notifications are enabled, showing notification...")
                
                // Show the notification
                notificationService.showTaskNotification(
                    taskId = taskId,
                    taskName = taskName,
                    notificationId = notificationId
                )
                
                Log.d(TAG, "🎉 Notification shown successfully for task: $taskName")
                Log.d(TAG, "=========================================")
                
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error processing notification for task $taskId", e)
                Log.e(TAG, "Exception details: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}