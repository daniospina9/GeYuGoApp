package com.example.geyugoapp.ui.utils.tasks

import com.example.geyugoapp.domain.task.models.Task
import java.util.Calendar

fun getDateFromTask(
    task: Task
): String {

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = task.dateTime

    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH) + 1
    val year = calendar.get(Calendar.YEAR)

    return "$dayOfMonth/$month/$year"
}