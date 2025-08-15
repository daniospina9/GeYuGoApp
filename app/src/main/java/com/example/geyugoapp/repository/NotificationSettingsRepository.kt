package com.example.geyugoapp.repository

import com.example.geyugoapp.domain.notifications.models.NotificationSettings
import kotlinx.coroutines.flow.Flow

interface NotificationSettingsRepository {
    suspend fun getNotificationSettingsByUserId(userId: Long): NotificationSettings?
    fun observeNotificationSettingsByUserId(userId: Long): Flow<NotificationSettings?>
    suspend fun insertNotificationSettings(notificationSettings: NotificationSettings): Long
    suspend fun updateNotificationSettings(notificationSettings: NotificationSettings)
    suspend fun deleteNotificationSettingsByUserId(userId: Long)
}