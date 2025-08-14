package com.example.geyugoapp.ui.utils.tasks

import com.example.geyugoapp.domain.task.models.Task

fun filterByCategory(
    tasksByUserId: List<Task>,
    idCategory: Long
): List<Task> {
    val tasksForDay = tasksByUserId.filter { task ->
        task.categoryId == idCategory
    }.sortedBy { task -> task.dateTime }

    return tasksForDay
}