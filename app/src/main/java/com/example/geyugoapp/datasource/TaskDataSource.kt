package com.example.geyugoapp.datasource

import com.example.geyugoapp.domain.task.models.Task

interface TaskDataSource {

    suspend fun insert(task: Task)
}