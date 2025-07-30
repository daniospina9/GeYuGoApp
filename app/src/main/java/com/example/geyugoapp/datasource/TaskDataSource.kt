package com.example.geyugoapp.datasource

import com.example.geyugoapp.domain.task.models.Task
import kotlinx.coroutines.flow.Flow

interface TaskDataSource {

    suspend fun insert(task: Task)

    fun observeAll(): Flow<List<Task>>

    suspend fun getTasksByUserId(userId: Long): List<Task>
}