package com.example.geyugoapp.repository

import com.example.geyugoapp.datasource.TaskDataSource
import com.example.geyugoapp.domain.task.models.Task
import kotlinx.coroutines.flow.Flow

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource
): TaskRepository {

    override suspend fun insert(task: Task) {
        taskDataSource.insert(task)
    }

    override fun observeAll(): Flow<List<Task>> = taskDataSource.observeAll()

    override suspend fun getTasksByUserId(userId: Long): List<Task> {
        return taskDataSource.getTasksByUserId(userId)
    }
}