package com.example.geyugoapp.domain.notifications.usecases

import com.example.geyugoapp.domain.notifications.models.NotificationSettings
import com.example.geyugoapp.repository.NotificationSettingsRepository
import javax.inject.Inject

class GetNotificationSettingsByUserIdUseCase @Inject constructor(
    private val notificationSettingsRepository: NotificationSettingsRepository
) {
    
    suspend operator fun invoke(userId: Long): NotificationSettings? {
        return notificationSettingsRepository.getNotificationSettingsByUserId(userId)
    }
}