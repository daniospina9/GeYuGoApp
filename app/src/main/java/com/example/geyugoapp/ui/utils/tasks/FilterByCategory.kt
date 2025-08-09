package com.example.geyugoapp.ui.util.tasks

import androidx.compose.runtime.Composable
import com.example.geyugoapp.domain.task.models.Task

@Composable
fun filterByCategory(
    tasksByUserId: List<Task>,
    idCategory: Long
): List<Task> {
    val tasksForDay = tasksByUserId.filter { task ->
        task.categoryId == idCategory
    }.sortedBy { task -> task.dateTime }

    return tasksForDay
}