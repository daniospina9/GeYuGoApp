package com.example.geyugoapp.domain.task.usecases

import androidx.room.util.copy
import com.example.geyugoapp.domain.notifications.usecases.GetNotificationSettingsByUserId
import com.example.geyugoapp.domain.task.models.Task
import com.example.geyugoapp.notifications.TaskNotificationManager
import com.example.geyugoapp.repository.TaskRepository

class InsertTask(
    private val repository: TaskRepository,
    private val notificationManager: TaskNotificationManager,
    private val getNotificationSettings: GetNotificationSettingsByUserId
) {
    suspend operator fun invoke(task: Task): Task {
        // Insert task first to get the ID
        val insertedTask = repository.insert(task)

        // Check if notifications are enabled for this user
        val notificationSettings = getNotificationSettings(task.userId)

        return if (notificationSettings?.areNotificationsEnabled == true) {
            // Schedule notification and update task with notification ID
            val notificationId = notificationManager.scheduleNotification(insertedTask)
            val taskWithNotification = insertedTask.copy(notificationId = notificationId)
            repository.updateTask(taskWithNotification)
            taskWithNotification
        } else {
            insertedTask
        }
    }
}