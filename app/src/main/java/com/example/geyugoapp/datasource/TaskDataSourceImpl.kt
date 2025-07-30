package com.example.geyugoapp.datasource

import com.example.geyugoapp.database.TaskDao
import com.example.geyugoapp.datasource.converters.toDbDto
import com.example.geyugoapp.datasource.converters.toTask
import com.example.geyugoapp.domain.task.models.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskDataSourceImpl(
    private val taskDao: TaskDao
): TaskDataSource {

    override suspend fun insert(task: Task) {
        taskDao.insert(task.toDbDto())
    }

    override fun observeAll(): Flow<List<Task>> = taskDao.observeAll().map { listFlow ->
        listFlow.map { it.toTask() }
    }

    override suspend fun getTasksByUserId(userId: Long): List<Task> {
        return taskDao.getTasksByUserId(userId).map { it.toTask() }
    }
}