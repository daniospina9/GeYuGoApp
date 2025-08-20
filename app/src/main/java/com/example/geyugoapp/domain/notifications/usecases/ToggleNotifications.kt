package com.example.geyugoapp.domain.notifications.usecases

import com.example.geyugoapp.domain.notifications.models.NotificationSettings
import com.example.geyugoapp.domain.task.usecases.GetTasksByUserId
import com.example.geyugoapp.domain.task.usecases.UpdateTask
import com.example.geyugoapp.notifications.TaskNotificationManager
import com.example.geyugoapp.repository.NotificationSettingsRepository
import javax.inject.Inject

class ToggleNotifications @Inject constructor(
    private val notificationSettingsRepository: NotificationSettingsRepository,
    private val getTasksByUserId: GetTasksByUserId,
    private val updateTask: UpdateTask,
    private val notificationManager: TaskNotificationManager
) {

    suspend operator fun invoke(userId: Long, enable: Boolean): Result<NotificationSettings> {
        return try {
            // Get current settings or create new ones
            val currentSettings = notificationSettingsRepository.getNotificationSettingsByUserId(userId)
            val newSettings = currentSettings?.copy(areNotificationsEnabled = enable)
                ?: NotificationSettings(userId = userId, areNotificationsEnabled = enable)

            // Update settings in database
            if (currentSettings == null) {
                notificationSettingsRepository.insertNotificationSettings(newSettings)
            } else {
                notificationSettingsRepository.updateNotificationSettings(newSettings)
            }

            // Get all tasks for this user
            val userTasks = getTasksByUserId(userId)

            if (enable) {
                // Enable notifications: schedule all future tasks
                val updatedTasks = notificationManager.rescheduleAllNotifications(userTasks)
                updatedTasks.forEach { task ->
                    updateTask(task)
                }
            } else {
                // Disable notifications: cancel all and clear notification IDs
                notificationManager.cancelAllNotifications(userTasks)
                userTasks.forEach { task ->
                    if (task.notificationId != null) {
                        updateTask(task.copy(notificationId = null))
                    }
                }
            }

            Result.success(newSettings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}