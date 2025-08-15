package com.example.geyugoapp.di

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import androidx.room.Room
import com.example.geyugoapp.database.AppDatabase
import com.example.geyugoapp.database.NotificationSettingsDao
import com.example.geyugoapp.database.UserDao
import com.example.geyugoapp.datasource.NotificationSettingsDataSource
import com.example.geyugoapp.datasource.NotificationSettingsDataSourceImpl
import com.example.geyugoapp.datasource.UserDataSource
import com.example.geyugoapp.datasource.UserDataSourceImpl
import com.example.geyugoapp.notifications.NotificationService
import com.example.geyugoapp.notifications.TaskNotificationManager
import com.example.geyugoapp.repository.NotificationSettingsRepository
import com.example.geyugoapp.repository.NotificationSettingsRepositoryImpl
import com.example.geyugoapp.repository.UserRepository
import com.example.geyugoapp.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
       @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java, "user-db"
    ).build()

    @Singleton
    @Provides
    fun provideUserDao(
        appDatabase: AppDatabase
    ): UserDao = appDatabase.userDao()

    @Singleton
    @Provides
    fun provideUserDataSource(
        userDao: UserDao
    ): UserDataSource = UserDataSourceImpl(
        userDao = userDao
    )

    @Singleton
    @Provides
    fun provideUserRepository(
        userDataSource: UserDataSource
    ): UserRepository = UserRepositoryImpl(
        userDataSource = userDataSource
    )

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
}