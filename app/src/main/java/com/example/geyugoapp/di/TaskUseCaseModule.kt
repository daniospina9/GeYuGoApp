package com.example.geyugoapp.di

import com.example.geyugoapp.database.AppDatabase
import com.example.geyugoapp.database.TaskDao
import com.example.geyugoapp.datasource.TaskDataSource
import com.example.geyugoapp.datasource.TaskDataSourceImpl
import com.example.geyugoapp.domain.task.usecases.InsertTask
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
        repository: TaskRepository
    ): InsertTask = InsertTask(
        repository = repository
    )
}