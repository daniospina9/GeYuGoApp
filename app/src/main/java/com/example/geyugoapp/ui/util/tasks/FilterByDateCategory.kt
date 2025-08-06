package com.example.geyugoapp.ui.util.tasks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.geyugoapp.domain.task.models.Task
import java.util.Calendar

@Composable
fun filterByDateCategory(
    date: Long?,
    tasksByUserId: List<Task>
): List<Task> {
    val (filterStartOfDayMillis, filterEndOfDayMillis) = remember(date) {
        if (date != null) {
            val startCalendar = Calendar.getInstance()
            startCalendar.timeInMillis = date
            startCalendar.set(Calendar.HOUR_OF_DAY, 0)
            startCalendar.set(Calendar.MINUTE, 0)
            startCalendar.set(Calendar.SECOND, 0)
            startCalendar.set(Calendar.MILLISECOND, 0)
            val start = startCalendar.timeInMillis

            val endCalendar = Calendar.getInstance()
            endCalendar.timeInMillis = date
            endCalendar.set(Calendar.HOUR_OF_DAY, 23)
            endCalendar.set(Calendar.MINUTE, 59)
            endCalendar.set(Calendar.SECOND, 59)
            endCalendar.set(Calendar.MILLISECOND, 999)
            val end = endCalendar.timeInMillis

            Pair(start, end)
        } else {
            Pair(null, null)
        }
    }

    val tasksForDay =
        remember(tasksByUserId, filterStartOfDayMillis, filterEndOfDayMillis) {
            val start = filterStartOfDayMillis
            val end = filterEndOfDayMillis

            if (start != null && end != null) {
                tasksByUserId.filter { task ->
                    task.dateTime >= start && task.dateTime <= end
                }.sortedBy { task -> task.dateTime }
            } else {
                emptyList()
            }
        }
    return tasksForDay
}