package com.example.geyugoapp.domain.task.usecases

import com.example.geyugoapp.repository.TaskRepository

class GetCountTasksByCategory(
    private val repository: TaskRepository
) {
    suspend open fun invoke(categoryId: Long): Int {
        return repository.getCountTasksByCategory(categoryId)
    }
}