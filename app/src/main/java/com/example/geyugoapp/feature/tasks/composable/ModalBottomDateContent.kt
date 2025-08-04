package com.example.geyugoapp.feature.tasks.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.geyugoapp.feature.tasks.TasksViewModel
import com.example.geyugoapp.ui.theme.BackgroundLevel3
import java.util.Calendar

@Composable
fun ModalBottomDateContent(
    viewModel: TasksViewModel = hiltViewModel(),
    date: Long?
) {
    val tasksByUserId by viewModel.tasksByUserId.collectAsStateWithLifecycle()

    val categoriesByUser by viewModel.categoriesByUser.collectAsStateWithLifecycle()

    val (filterStartOfDayMillis, filterEndOfDayMillis) = remember(date) {
        if (date != null) {

            val start = date

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            val end = calendar.timeInMillis

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

    LazyColumnTasksContent(
        tasksForDay = tasksForDay,
        categoriesByUser = categoriesByUser,
        startPadding = 24.dp,
        endPadding = 24.dp,
        bottomPadding = 24.dp,
        backgroundColor = BackgroundLevel3
    )
}