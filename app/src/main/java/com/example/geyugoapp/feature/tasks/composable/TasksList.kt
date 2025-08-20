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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.geyugoapp.R
import com.example.geyugoapp.domain.categories.models.Category
import com.example.geyugoapp.domain.task.models.Task
import com.example.geyugoapp.feature.tasks.TasksViewModel
import com.example.geyugoapp.ui.theme.BackgroundLevel2
import com.example.geyugoapp.ui.theme.FirstUserButton
import com.example.geyugoapp.ui.utils.tasks.tasksListDetails

@Composable
fun TasksList(
    viewModel: TasksViewModel = hiltViewModel(),
    tasksForDay: List<Task>,
    categoriesByUser: List<Category>,
    startPadding: Dp,
    endPadding: Dp,
    bottomPadding: Dp,
    backgroundColor: Long
) {

    val tasksListByDateState by viewModel.tasksListByDateState.collectAsStateWithLifecycle()

    val currentTaskByDelete = tasksListByDateState.taskListTaskByDelete

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
            val details = tasksListDetails(
                taskItem = taskItem,
                categoriesByUser = categoriesByUser
            )
            val backgroundCircleTask = details.backgroundCircleTask
            val borderCircleTaskWidth = details.borderCircleTaskWidth
            val colorBox = details.colorBox
            val lineThroughTask = details.lineThroughTask
            val formattedTime = details.formattedTime
            val amPm = details.amPm
            val timeTaskColor = details.timeTaskColor
            var expanded by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = BackgroundLevel2,
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
                                contentDescription = null,
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
                                viewModel.setTaskByDelete(taskItem)
                                viewModel.setShowListByDateDialog(true)
                                expanded = false
                            },
                        text = stringResource(R.string.delete_task),
                    )
                }
            }
        }
    }
    if (tasksListByDateState.showListByDateDialog && currentTaskByDelete != null) {
        AlertDialog(
            onDismissRequest = {
                viewModel.setShowListByDateDialog(false)
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.removeTask(currentTaskByDelete)
                        viewModel.setShowListByDateDialog(false)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FirstUserButton,
                    )
                ) {
                    Text(
                        text = stringResource(R.string.confirm)
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        viewModel.setShowListByDateDialog(false)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                    )
                ) {
                    Text(
                        text = stringResource(R.string.abort),
                        color = FirstUserButton
                    )
                }
            },
            text = {
                Text(
                    text = stringResource(R.string.if_you_delete_this_task_you_won_t_be_able_to_recover_it_do_you_want_to_continue),
                    textAlign = TextAlign.Left
                )
            },
            title = {
                Text(
                    text = stringResource(R.string.are_you_sure),
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