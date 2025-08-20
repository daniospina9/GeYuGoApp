package com.example.geyugoapp.domain.notifications.usecases

import com.example.geyugoapp.domain.notifications.models.NotificationSettings
import com.example.geyugoapp.repository.NotificationSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveNotificationSettingsByUserId @Inject constructor(
    private val notificationSettingsRepository: NotificationSettingsRepository
) {

    operator fun invoke(userId: Long): Flow<NotificationSettings?> {
        return notificationSettingsRepository.observeNotificationSettingsByUserId(userId)
    }
}