package com.example.geyugoapp.feature.tasks.composable

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.geyugoapp.feature.tasks.TasksViewModel
import com.example.geyugoapp.ui.theme.BackgroundLevel3
import com.example.geyugoapp.ui.util.tasks.filterByCategory
import com.example.geyugoapp.ui.util.tasks.getDatesList

@Composable
fun FilterByCategory(
    viewModel: TasksViewModel = hiltViewModel(),
    idCategory: Long
) {
    val tasksByUserId by viewModel.tasksByUserId.collectAsStateWithLifecycle()

    val categoriesByUser by viewModel.categoriesByUser.collectAsStateWithLifecycle()

    val TAG = "ListDateCategory"

    val tasksForCategory = filterByCategory(
        tasksByUserId = tasksByUserId,
        idCategory = idCategory
    )

    val datesList = getDatesList( tasksForCategory = tasksForCategory)

    Log.d(TAG, "Botón 'Your Categories' clickeado. Valor de userId: $datesList")

    TasksListByCategory(
        tasksForDay = tasksForCategory,
        categoriesByUser = categoriesByUser,
        startPadding = 24.dp,
        endPadding = 24.dp,
        bottomPadding = 24.dp,
        backgroundColor = BackgroundLevel3,
        datesList = datesList
    )
}