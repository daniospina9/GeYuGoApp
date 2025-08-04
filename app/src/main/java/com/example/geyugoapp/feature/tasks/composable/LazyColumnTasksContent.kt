package com.example.geyugoapp.feature.tasks.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration.Companion.LineThrough
import androidx.compose.ui.text.style.TextDecoration.Companion.None
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.geyugoapp.R
import com.example.geyugoapp.domain.categories.models.Category
import com.example.geyugoapp.domain.task.models.Task
import com.example.geyugoapp.feature.tasks.TasksViewModel
import com.example.geyugoapp.ui.theme.BackgroundLevel1
import com.example.geyugoapp.ui.theme.BackgroundLevel2
import com.example.geyugoapp.ui.theme.FirstUserButton
import com.example.geyugoapp.ui.theme.FramePhotoProfile
import java.util.Calendar

@Composable
fun LazyColumnTasksContent(
    viewModel: TasksViewModel = hiltViewModel(),
    tasksForDay: List<Task>,
    categoriesByUser: List<Category>,
    startPadding: Dp,
    endPadding: Dp,
    bottomPadding: Dp,
    backgroundColor: Long
) {
    var taskByDelete by remember { mutableStateOf<Task?>(null) }

    val currentTaskByDelete = taskByDelete

    var showDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(backgroundColor))
            .padding(start = startPadding, end = endPadding, bottom = bottomPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            count = tasksForDay.size
        ) { index ->
            val taskItem = tasksForDay[index]
            val matchingCategory = categoriesByUser.find { category ->
                category.id == taskItem.categoryId
            }
            val colorBox = matchingCategory?.color ?: FramePhotoProfile
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
                if ("$formattedTime $amPm" == "11:59 PM") Color(BackgroundLevel2)
                else Color.White
            val backgroundCircleTask =
                if (taskItem.isClicked) BackgroundLevel1 else BackgroundLevel2
            val borderCircleTaskWidth = if (taskItem.isClicked) 0.dp else 2.dp
            val lineThroughTask = if (taskItem.isClicked) LineThrough else None
            var expanded by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(BackgroundLevel2),
                        shape = RoundedCornerShape(15.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    expanded = true
                                }
                            )
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(23.dp)
                            .background(
                                color = Color(backgroundCircleTask),
                                shape = CircleShape
                            )
                            .border(
                                width = borderCircleTaskWidth,
                                color = Color(colorBox),
                                shape = CircleShape
                            )
                            .clickable {
                                viewModel.updateCurrentTask(taskItem)
                            }
                    ) {
                        if (taskItem.isClicked) {
                            Image(
                                painter = painterResource(R.drawable.check),
                                modifier = Modifier
                                    .size(25.dp),
                                contentDescription = "A check logo to indicate the task state",
                                contentScale = ContentScale.Inside,
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(
                        modifier = Modifier.weight(0.7f),
                        text = taskItem.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                        fontSize = 15.sp,
                        style = TextStyle(
                            textDecoration = lineThroughTask
                        )
                    )
                    Text(
                        modifier = Modifier.weight(0.18f),
                        text = "$formattedTime $amPm",
                        color = timeTaskColor,
                        fontSize = 10.sp,
                        style = TextStyle(
                            textDecoration = lineThroughTask
                        )
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    offset = DpOffset(135.dp, 0.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 6.dp, end = 6.dp)
                            .clickable {
                                taskByDelete = taskItem
                                showDialog = true
                                expanded = false
                            },
                        text = "Delete task",
                    )
                }
            }
        }
    }
    if (showDialog && currentTaskByDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.removeTask(currentTaskByDelete)
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(FirstUserButton),
                    )
                ) {
                    Text(
                        text = "Confirm"
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                    )
                ) {
                    Text(
                        text = "Abort",
                        color = Color(FirstUserButton)
                    )
                }
            },
            text = {
                Text(
                    text = "If you delete this task, you won't be able to recover it. Do you want to continue?",
                    textAlign = TextAlign.Left
                )
            },
            title = {
                Text(
                    text = "Are you sure?",
                )
            },
            shape = RoundedCornerShape(15.dp),
            containerColor = Color.White,
            textContentColor = Color.Black,
            tonalElevation = 12.dp,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                securePolicy = SecureFlagPolicy.Inherit,
                usePlatformDefaultWidth = true,
                decorFitsSystemWindows = true
            )
        )
    }
}