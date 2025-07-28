package com.example.geyugoapp.repository

import com.example.geyugoapp.domain.task.models.Task

interface TaskRepository {

    suspend fun insert(task: Task)
}