package com.example.geyugoapp.domain.task.usecases

import com.example.geyugoapp.domain.task.models.Task
import com.example.geyugoapp.repository.TaskRepository

class GetTasksByUserId(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(userId: Long): List<Task> {
        return repository.getTasksByUserId(userId)
    }
}