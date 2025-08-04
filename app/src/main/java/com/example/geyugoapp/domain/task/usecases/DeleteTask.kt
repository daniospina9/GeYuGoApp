package com.example.geyugoapp.domain.task.usecases

import com.example.geyugoapp.domain.task.models.Task
import com.example.geyugoapp.repository.TaskRepository

class DeleteTask(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        repository.deleteTask(task)
    }
}