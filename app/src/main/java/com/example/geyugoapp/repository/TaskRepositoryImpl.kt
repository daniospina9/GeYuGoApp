package com.example.geyugoapp.repository

import com.example.geyugoapp.datasource.TaskDataSource
import com.example.geyugoapp.domain.task.models.Task

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource
): TaskRepository {

    override suspend fun insert(task: Task) {
        taskDataSource.insert(task)
    }
}