package com.example.geyugoapp.domain.notifications.models

data class NotificationSettings(
    val id: Long = 0L,
    val userId: Long,
    val areNotificationsEnabled: Boolean = false
)