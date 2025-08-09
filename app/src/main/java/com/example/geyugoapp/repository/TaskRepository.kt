package com.example.geyugoapp.repository

import com.example.geyugoapp.domain.task.models.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun insert(task: Task)

    suspend fun deleteTask(task: Task)

    fun observeAll(): Flow<List<Task>>

    suspend fun getTasksByUserId(userId: Long): List<Task>

    suspend fun updateTask(task: Task)

    suspend fun getCountTasksByCategory(categoryId: Long): Int
}