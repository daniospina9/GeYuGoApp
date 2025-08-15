package com.example.geyugoapp.di

import com.example.geyugoapp.domain.notifications.usecases.GetNotificationSettingsByUserIdUseCase
import com.example.geyugoapp.domain.notifications.usecases.ObserveNotificationSettingsByUserIdUseCase
import com.example.geyugoapp.domain.notifications.usecases.ToggleNotificationsUseCase
import com.example.geyugoapp.domain.task.usecases.GetTasksByUserId
import com.example.geyugoapp.domain.task.usecases.UpdateTask
import com.example.geyugoapp.notifications.TaskNotificationManager
import com.example.geyugoapp.repository.NotificationSettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NotificationUseCaseModule {

    @Provides
    fun provideGetNotificationSettingsByUserIdUseCase(
        repository: NotificationSettingsRepository
    ): GetNotificationSettingsByUserIdUseCase {
        return GetNotificationSettingsByUserIdUseCase(repository)
    }

    @Provides
    fun provideObserveNotificationSettingsByUserIdUseCase(
        repository: NotificationSettingsRepository
    ): ObserveNotificationSettingsByUserIdUseCase {
        return ObserveNotificationSettingsByUserIdUseCase(repository)
    }

    @Provides
    fun provideToggleNotificationsUseCase(
        repository: NotificationSettingsRepository,
        getTasksByUserId: GetTasksByUserId,
        updateTask: UpdateTask,
        notificationManager: TaskNotificationManager
    ): ToggleNotificationsUseCase {
        return ToggleNotificationsUseCase(repository, getTasksByUserId, updateTask, notificationManager)
    }
}