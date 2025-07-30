package com.example.geyugoapp.domain.task.usecases

import com.example.geyugoapp.domain.task.models.Task
import com.example.geyugoapp.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class ObserveAll(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> = repository.observeAll()
}