package com.example.geyugoapp.domain.task.usecases

import com.example.geyugoapp.domain.task.models.Task
import com.example.geyugoapp.repository.TaskRepository

class UpdateTask(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        repository.updateTask(task)
    }
}