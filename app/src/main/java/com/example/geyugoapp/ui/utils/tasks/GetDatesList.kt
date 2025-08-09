package com.example.geyugoapp.ui.util.tasks

import androidx.compose.runtime.Composable
import com.example.geyugoapp.domain.task.models.Task
import java.util.Calendar

@Composable
fun getDatesList(
    tasksForCategory: List<Task>
): List<String> {

    if (tasksForCategory.isEmpty()) {
        return emptyList()
    }

    val uniqueTimestamps = tasksForCategory
        .asSequence()
        .filter { it.dateTime > 0L }
        .map { task -> task.dateTime }
        .distinct()
        .toList()

    if (uniqueTimestamps.isEmpty()) {
        return emptyList()
    }

    val uniqueDateStringsSet = mutableSetOf<String>()
    val calendar = Calendar.getInstance()

    uniqueTimestamps.forEach { timestamp ->
        calendar.timeInMillis = timestamp

        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)

        uniqueDateStringsSet.add("$dayOfMonth/$month/$year")
    }

    return uniqueDateStringsSet.toList().sortedWith(compareBy { dateString ->
        val parts = dateString.split("/").map { it.toInt() }
        String.format("%02d-%02d-%04d", parts[0], parts[1], parts[2])
    })

}