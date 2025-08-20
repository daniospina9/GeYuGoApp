package com.example.geyugoapp.di

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import com.example.geyugoapp.database.AppDatabase
import com.example.geyugoapp.database.NotificationSettingsDao
import com.example.geyugoapp.datasource.NotificationSettingsDataSource
import com.example.geyugoapp.datasource.converters.NotificationSettingsDataSourceImpl
import com.example.geyugoapp.domain.notifications.usecases.GetNotificationSettingsByUserId
import com.example.geyugoapp.domain.notifications.usecases.ObserveNotificationSettingsByUserId
import com.example.geyugoapp.domain.notifications.usecases.ToggleNotifications
import com.example.geyugoapp.domain.task.usecases.GetTasksByUserId
import com.example.geyugoapp.domain.task.usecases.UpdateTask
import com.example.geyugoapp.notifications.NotificationService
import com.example.geyugoapp.notifications.TaskNotificationManager
import com.example.geyugoapp.repository.NotificationSettingsRepository
import com.example.geyugoapp.repository.NotificationSettingsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationUseCaseModule {

    @Singleton
    @Provides
    fun provideNotificationSettingsDao(
        appDatabase: AppDatabase
    ): NotificationSettingsDao = appDatabase.notificationSettingsDao()

    @Singleton
    @Provides
    fun provideNotificationSettingsDataSource(
        notificationSettingsDao: NotificationSettingsDao
    ): NotificationSettingsDataSource = NotificationSettingsDataSourceImpl(
        notificationSettingsDao = notificationSettingsDao
    )

    @Singleton
    @Provides
    fun provideNotificationSettingsRepository(
        notificationSettingsDataSource: NotificationSettingsDataSource
    ): NotificationSettingsRepository = NotificationSettingsRepositoryImpl(
        notificationSettingsDataSource = notificationSettingsDataSource
    )

    @Singleton
    @Provides
    fun provideAlarmManager(
        @ApplicationContext context: Context
    ): AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @Singleton
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Singleton
    @Provides
    fun provideNotificationService(
        @ApplicationContext context: Context,
        notificationManager: NotificationManager
    ): NotificationService = NotificationService(context, notificationManager)

    @Singleton
    @Provides
    fun provideTaskNotificationManager(
        @ApplicationContext context: Context,
        alarmManager: AlarmManager
    ): TaskNotificationManager = TaskNotificationManager(context, alarmManager)

    @Provides
    fun provideGetNotificationSettingsByUserIdUseCase(
        repository: NotificationSettingsRepository
    ): GetNotificationSettingsByUserId {
        return GetNotificationSettingsByUserId(repository)
    }

    @Provides
    fun provideObserveNotificationSettingsByUserIdUseCase(
        repository: NotificationSettingsRepository
    ): ObserveNotificationSettingsByUserId {
        return ObserveNotificationSettingsByUserId(repository)
    }

    @Provides
    fun provideToggleNotificationsUseCase(
        repository: NotificationSettingsRepository,
        getTasksByUserId: GetTasksByUserId,
        updateTask: UpdateTask,
        notificationManager: TaskNotificationManager
    ): ToggleNotifications {
        return ToggleNotifications(repository, getTasksByUserId, updateTask, notificationManager)
    }
}