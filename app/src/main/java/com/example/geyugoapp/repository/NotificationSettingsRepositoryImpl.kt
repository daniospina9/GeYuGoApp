package com.example.geyugoapp.repository

import com.example.geyugoapp.datasource.NotificationSettingsDataSource
import com.example.geyugoapp.domain.notifications.models.NotificationSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationSettingsRepositoryImpl @Inject constructor(
    private val notificationSettingsDataSource: NotificationSettingsDataSource
) : NotificationSettingsRepository {
    
    override suspend fun getNotificationSettingsByUserId(userId: Long): NotificationSettings? {
        return notificationSettingsDataSource.getNotificationSettingsByUserId(userId)
    }
    
    override fun observeNotificationSettingsByUserId(userId: Long): Flow<NotificationSettings?> {
        return notificationSettingsDataSource.observeNotificationSettingsByUserId(userId)
    }
    
    override suspend fun insertNotificationSettings(notificationSettings: NotificationSettings): Long {
        return notificationSettingsDataSource.insertNotificationSettings(notificationSettings)
    }
    
    override suspend fun updateNotificationSettings(notificationSettings: NotificationSettings) {
        notificationSettingsDataSource.updateNotificationSettings(notificationSettings)
    }
    
    override suspend fun deleteNotificationSettingsByUserId(userId: Long) {
        notificationSettingsDataSource.deleteNotificationSettingsByUserId(userId)
    }
}