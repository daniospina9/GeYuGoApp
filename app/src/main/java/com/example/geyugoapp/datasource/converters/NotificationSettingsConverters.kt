package com.example.geyugoapp.datasource.converters

import com.example.geyugoapp.database.dtos.NotificationSettingsDbDto
import com.example.geyugoapp.domain.notifications.models.NotificationSettings

fun NotificationSettings.toDbDto(): NotificationSettingsDbDto {
    return NotificationSettingsDbDto(
        id = id,
        userId = userId,
        areNotificationsEnabled = areNotificationsEnabled
    )
}

fun NotificationSettingsDbDto.toNotificationSettings(): NotificationSettings {
    return NotificationSettings(
        id = id,
        userId = userId,
        areNotificationsEnabled = areNotificationsEnabled
    )
}