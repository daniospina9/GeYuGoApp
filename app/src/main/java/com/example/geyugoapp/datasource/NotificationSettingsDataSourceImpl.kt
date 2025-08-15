package com.example.geyugoapp.datasource

import com.example.geyugoapp.database.NotificationSettingsDao
import com.example.geyugoapp.datasource.converters.toDbDto
import com.example.geyugoapp.datasource.converters.toNotificationSettings
import com.example.geyugoapp.domain.notifications.models.NotificationSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationSettingsDataSourceImpl @Inject constructor(
    private val notificationSettingsDao: NotificationSettingsDao
) : NotificationSettingsDataSource {
    
    override suspend fun getNotificationSettingsByUserId(userId: Long): NotificationSettings? {
        return notificationSettingsDao.getNotificationSettingsByUserId(userId)?.toNotificationSettings()
    }
    
    override fun observeNotificationSettingsByUserId(userId: Long): Flow<NotificationSettings?> {
        return notificationSettingsDao.observeNotificationSettingsByUserId(userId)
            .map { it?.toNotificationSettings() }
    }
    
    override suspend fun insertNotificationSettings(notificationSettings: NotificationSettings): Long {
        return notificationSettingsDao.insertNotificationSettings(notificationSettings.toDbDto())
    }
    
    override suspend fun updateNotificationSettings(notificationSettings: NotificationSettings) {
        notificationSettingsDao.updateNotificationSettings(notificationSettings.toDbDto())
    }
    
    override suspend fun deleteNotificationSettingsByUserId(userId: Long) {
        notificationSettingsDao.deleteNotificationSettingsByUserId(userId)
    }
}