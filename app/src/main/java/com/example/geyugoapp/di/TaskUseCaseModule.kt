package com.example.geyugoapp.di

import com.example.geyugoapp.database.AppDatabase
import com.example.geyugoapp.database.TaskDao
import com.example.geyugoapp.datasource.TaskDataSource
import com.example.geyugoapp.datasource.TaskDataSourceImpl
import com.example.geyugoapp.domain.notifications.usecases.GetNotificationSettingsByUserIdUseCase
import com.example.geyugoapp.domain.task.usecases.DeleteTask
import com.example.geyugoapp.domain.task.usecases.GetCountTasksByCategory
import com.example.geyugoapp.domain.task.usecases.GetTasksByUserId
import com.example.geyugoapp.domain.task.usecases.InsertTask
import com.example.geyugoapp.domain.task.usecases.ObserveAll
import com.example.geyugoapp.domain.task.usecases.UpdateTask
import com.example.geyugoapp.notifications.TaskNotificationManager
import com.example.geyugoapp.repository.TaskRepository
import com.example.geyugoapp.repository.TaskRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TaskUseCaseModule {

    @Singleton
    @Provides
    fun provideTaskDao(
        appDatabase: AppDatabase
    ): TaskDao = appDatabase.taskDao()

    @Singleton
    @Provides
    fun provideTaskDataSource(
        taskDao: TaskDao
    ): TaskDataSource = TaskDataSourceImpl(
        taskDao = taskDao
    )

    @Singleton
    @Provides
    fun provideTaskRepository(
        taskDataSource: TaskDataSource
    ): TaskRepository = TaskRepositoryImpl(
        taskDataSource = taskDataSource
    )

    @Singleton
    @Provides
    fun provideInsertTask(
        repository: TaskRepository,
        notificationManager: TaskNotificationManager,
        getNotificationSettings: GetNotificationSettingsByUserIdUseCase
    ): InsertTask = InsertTask(
        repository = repository,
        notificationManager = notificationManager,
        getNotificationSettings = getNotificationSettings
    )

    @Singleton
    @Provides
    fun provideObserveAll(
        repository: TaskRepository
    ): ObserveAll = ObserveAll(
        repository = repository
    )

    @Singleton
    @Provides
    fun provideGetTasksByUserId(
        repository: TaskRepository
    ): GetTasksByUserId = GetTasksByUserId(
        repository = repository
    )

    @Singleton
    @Provides
    fun provideDeleteTask(
        repository: TaskRepository,
        notificationManager: TaskNotificationManager
    ): DeleteTask = DeleteTask(
        repository = repository,
        notificationManager = notificationManager
    )

    @Singleton
    @Provides
    fun provideUpdateTask(
        repository: TaskRepository
    ): UpdateTask = UpdateTask(
        repository = repository
    )

    @Singleton
    @Provides
    fun provideGetCountTasksByCategory(
        repository: TaskRepository
    ): GetCountTasksByCategory = GetCountTasksByCategory(
        repository = repository
    )
}