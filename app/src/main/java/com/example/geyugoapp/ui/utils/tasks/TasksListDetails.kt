package com.example.geyugoapp.ui.utils.tasks

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextDecoration.Companion.LineThrough
import androidx.compose.ui.text.style.TextDecoration.Companion.None
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.geyugoapp.domain.categories.models.Category
import com.example.geyugoapp.domain.task.models.Task
import com.example.geyugoapp.ui.theme.BackgroundLevel1
import com.example.geyugoapp.ui.theme.BackgroundLevel2
import com.example.geyugoapp.ui.theme.FramePhotoProfile
import java.util.Calendar

data class TasksDetails(
    val colorBox: Long,
    val formattedTime: String,
    val amPm: String,
    val timeTaskColor: Color,
    val backgroundCircleTask: Long,
    val borderCircleTaskWidth: Dp,
    val lineThroughTask: TextDecoration
        )

fun tasksListDetails(
    taskItem: Task,
    categoriesByUser: List<Category>
): TasksDetails {
    val matchingCategory = categoriesByUser.find { category ->
        category.id == taskItem.categoryId
    }
    val colorBox = matchingCategory?.color ?: FramePhotoProfile.value.toLong()
    val taskDateTimeMillis = taskItem.dateTime
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = taskDateTimeMillis
    val hour24 = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val hour12 = when {
        hour24 == 0 -> 12
        hour24 > 12 -> hour24 - 12
        else -> hour24
    }
    val amPm = if (hour24 < 12) "AM" else "PM"
    val formattedTime = String.format("%02d:%02d", hour12, minute)
    val timeTaskColor =
        if ("$formattedTime $amPm" == "11:59 PM") BackgroundLevel2
        else Color.White
    val backgroundCircleTask =
        if (taskItem.isClicked) BackgroundLevel1.value.toLong() else BackgroundLevel2.value.toLong()
    val borderCircleTaskWidth = if (taskItem.isClicked) 0.dp else 2.dp
    val lineThroughTask = if (taskItem.isClicked) LineThrough else None

    return TasksDetails(
        colorBox = colorBox,
        formattedTime = formattedTime,
        amPm = amPm,
        timeTaskColor = timeTaskColor,
        backgroundCircleTask = backgroundCircleTask,
        borderCircleTaskWidth = borderCircleTaskWidth,
        lineThroughTask = lineThroughTask
    )
}