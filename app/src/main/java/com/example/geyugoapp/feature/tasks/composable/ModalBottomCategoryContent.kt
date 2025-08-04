package com.example.geyugoapp.feature.tasks.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.geyugoapp.feature.tasks.TasksViewModel
import com.example.geyugoapp.ui.theme.BackgroundLevel3

@Composable
fun ModalBottomCategoryContent(
    viewModel: TasksViewModel = hiltViewModel(),
    idCategory: Long
) {
    val tasksByUserId by viewModel.tasksByUserId.collectAsStateWithLifecycle()

    val categoriesByUser by viewModel.categoriesByUser.collectAsStateWithLifecycle()

    val tasksForDay = tasksByUserId.filter { task ->
            task.categoryId == idCategory
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