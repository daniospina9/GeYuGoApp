package com.example.geyugoapp.datasource

import com.example.geyugoapp.domain.notifications.models.NotificationSettings
import kotlinx.coroutines.flow.Flow

interface NotificationSettingsDataSource {
    suspend fun getNotificationSettingsByUserId(userId: Long): NotificationSettings?
    fun observeNotificationSettingsByUserId(userId: Long): Flow<NotificationSettings?>
    suspend fun insertNotificationSettings(notificationSettings: NotificationSettings): Long
    suspend fun updateNotificationSettings(notificationSettings: NotificationSettings)
    suspend fun deleteNotificationSettingsByUserId(userId: Long)
}