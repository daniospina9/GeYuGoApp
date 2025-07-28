package com.example.geyugoapp.datasource

import com.example.geyugoapp.database.TaskDao
import com.example.geyugoapp.datasource.converters.toDbDto
import com.example.geyugoapp.domain.task.models.Task

class TaskDataSourceImpl(
    private val taskDao: TaskDao
): TaskDataSource {

    override suspend fun insert(task: Task) {
        taskDao.insert(task.toDbDto())
    }
}