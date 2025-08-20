package com.example.geyugoapp.domain.task.usecases

import com.example.geyugoapp.domain.task.models.Task
import com.example.geyugoapp.notifications.TaskNotificationManager
import com.example.geyugoapp.repository.TaskRepository

class DeleteTask(
    private val repository: TaskRepository,
    private val notificationManager: TaskNotificationManager
) {
    suspend operator fun invoke(task: Task) {
        // Cancel notification if it exists
        task.notificationId?.let { notificationId ->
            notificationManager.cancelNotification(notificationId)
        }

        // Delete the task
        repository.deleteTask(task)
    }
}