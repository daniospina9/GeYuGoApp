package com.example.geyugoapp.feature.tasks

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.geyugoapp.R
import com.example.geyugoapp.feature.tasks.composable.CategoryDragHandle
import com.example.geyugoapp.feature.tasks.composable.DateDragHandle
import com.example.geyugoapp.feature.tasks.composable.EmptyTasksContent
import com.example.geyugoapp.feature.tasks.composable.FilterByCategory
import com.example.geyugoapp.feature.tasks.composable.FilterByDate
import com.example.geyugoapp.feature.tasks.composable.ModalDrawerTasks
import com.example.geyugoapp.feature.tasks.composable.TasksList
import com.example.geyugoapp.ui.theme.BackgroundLevel2
import com.example.geyugoapp.ui.theme.BackgroundLevel3
import com.example.geyugoapp.ui.theme.CreateButtons
import com.example.geyugoapp.ui.theme.LinesCategories
import com.example.geyugoapp.ui.utils.tasks.filterTasksByDate
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    viewModel: TasksViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val density = LocalDensity.current

    val categoriesWithCounts by viewModel.categoriesWithCounts.collectAsStateWithLifecycle()
    val tasksByUserId by viewModel.tasksByUserId.collectAsStateWithLifecycle()
    val datesState by viewModel.datesState.collectAsStateWithLifecycle()
    val drawersTasksScreenState by viewModel.drawersTasksScreenState.collectAsStateWithLifecycle()
    val areNotificationsEnabled by viewModel.areNotificationsEnabled.collectAsStateWithLifecycle()

    val currentCategoryId = drawersTasksScreenState.categorySelectedIdTaskScreen

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = datesState.calendar.timeInMillis,
        initialDisplayMode = DisplayMode.Input
    )

    val todayCalendar = datesState.calendar2

    val tasksForDay = filterTasksByDate(
        date = todayCalendar.timeInMillis,
        tasksByUserId = tasksByUserId
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    // Permission launcher for notifications
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.enableNotificationsAfterPermission()
        } else {
            Toast.makeText(context, "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(key1 = context) {
        viewModel.events.collect { event ->
            when (event) {
                is TasksViewModel.Event.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is TasksViewModel.Event.RequestNotificationPermission -> {
                    // Check if permission is already granted
                    val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    } else {
                        true // Pre-Android 13 doesn't need runtime permission
                    }

                    if (hasPermission) {
                        viewModel.enableNotificationsAfterPermission()
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }
            }
        }
    }

    Scaffold { innerPadding ->
        ModalDrawerTasks(
            modifier = Modifier.padding(innerPadding),
            drawerState = drawerState
        ) {
            if (categoriesWithCounts.isNotEmpty() && tasksForDay.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackgroundLevel3)
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BackgroundLevel3)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(BackgroundLevel3),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Image(
                                painter = painterResource(R.drawable.back),
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable {
                                        navController.navigateUp()
                                    },
                                contentDescription = null,
                                contentScale = ContentScale.Inside,
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(BackgroundLevel3),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Row {
                                Image(
                                    painter = painterResource(R.drawable.calendar_search),
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {
                                            viewModel.setShowDateFilterDialog(true)
                                        },
                                    contentDescription = null,
                                    contentScale = ContentScale.Inside,
                                    colorFilter = ColorFilter.tint(Color.White)
                                )
                                Spacer(modifier = Modifier.width(15.dp))
                                Image(
                                    painter = painterResource(R.drawable.search),
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {},
                                    contentDescription = null,
                                    contentScale = ContentScale.Inside,
                                    colorFilter = ColorFilter.tint(Color.White)
                                )
                                Spacer(modifier = Modifier.width(15.dp))
                                Image(
                                    painter = painterResource(R.drawable.notification),
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {
                                            viewModel.toggleNotifications {}
                                        },
                                    contentDescription = null,
                                    contentScale = ContentScale.Inside,
                                    colorFilter = ColorFilter.tint(
                                        if (areNotificationsEnabled) Color.Green else Color.White
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(17.dp))
                    Text(
                        modifier = Modifier.padding(bottom = 20.dp),
                        text = stringResource(R.string.categories),
                        color = Color.White,
                        fontSize = 20.sp
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(
                            count = categoriesWithCounts.size
                        ) { index ->
                            val categoryItem = categoriesWithCounts[index]
                            Box(
                                modifier = Modifier
                                    .width((((screenWidth.value - 60) / 2) - 15).dp)
                                    .background(

                                        color = BackgroundLevel2,
                                        shape = RoundedCornerShape(15.dp)
                                    )
                                    .clickable {
                                        viewModel.setCategorySelectedTaskScreen(categoryItem.category.name)
                                        viewModel.setCategorySelectedIdTaskScreen(categoryItem.category.id)
                                        viewModel.setShowCategoryBottomSheet(true)
                                    }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(15.dp)
                                ) {
                                    Text(
                                        modifier = Modifier.padding(start = 6.dp),
                                        text = "${categoryItem.taskCount} ${stringResource(R.string.tasks)}",
                                        fontSize = 10.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        modifier = Modifier.padding(start = 6.dp),
                                        text = categoryItem.category.name,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.horizontal_line),
                                            modifier = Modifier
                                                .height(25.dp)
                                                .weight(1f),
                                            contentDescription = null,
                                            contentScale = ContentScale.FillWidth,
                                            colorFilter = ColorFilter.tint(Color(categoryItem.category.color))
                                        )
                                        Image(
                                            painter = painterResource(R.drawable.horizontal_line),
                                            modifier = Modifier
                                                .height(25.dp)
                                                .weight(1f),
                                            contentDescription = null,
                                            contentScale = ContentScale.FillWidth,
                                            colorFilter = ColorFilter.tint(LinesCategories)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                            .onGloballyPositioned { coordinates: LayoutCoordinates ->
                                val positionInRoot = coordinates.positionInRoot()
                                viewModel.setTitlePositionY(positionInRoot.y)
                            },
                        text = stringResource(R.string.today_s_tasks),
                        color = Color.White,
                        fontSize = 17.sp
                    )
                    TasksList(
                        tasksForDay = tasksForDay,
                        categoriesByUser = categoriesWithCounts.map { it.category },
                        startPadding = 0.dp,
                        endPadding = 0.dp,
                        bottomPadding = 0.dp,
                        backgroundColor = BackgroundLevel3.toArgb().toLong()
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(
                                color = CreateButtons,
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    drawerState.open()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier.size(45.dp),
                            painter = painterResource(R.drawable.add),
                            contentDescription = null,
                            contentScale = ContentScale.Inside,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                }
            }
            else if (categoriesWithCounts.isEmpty() && tasksForDay.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackgroundLevel3)
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BackgroundLevel3)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(BackgroundLevel3),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Image(
                                painter = painterResource(R.drawable.back),
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable {
                                        navController.navigateUp()
                                    },
                                contentDescription = null,
                                contentScale = ContentScale.Inside,
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(BackgroundLevel3),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Row {
                                Image(
                                    painter = painterResource(R.drawable.calendar_search),
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {},
                                    contentDescription = null,
                                    contentScale = ContentScale.Inside,
                                    colorFilter = ColorFilter.tint(Color.White)
                                )
                                Spacer(modifier = Modifier.width(15.dp))
                                Image(
                                    painter = painterResource(R.drawable.search),
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {},
                                    contentDescription = null,
                                    contentScale = ContentScale.Inside,
                                    colorFilter = ColorFilter.tint(Color.White)
                                )
                                Spacer(modifier = Modifier.width(15.dp))
                                Image(
                                    painter = painterResource(R.drawable.notification),
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {
                                            viewModel.toggleNotifications {}
                                        },
                                    contentDescription = null,
                                    contentScale = ContentScale.Inside,
                                    colorFilter = ColorFilter.tint(
                                        if (areNotificationsEnabled) Color.Green else Color.White
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    EmptyTasksContent(
                        addingTaskClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    )
                }
            }
            else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackgroundLevel3)
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BackgroundLevel3)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(BackgroundLevel3),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Image(
                                painter = painterResource(R.drawable.back),
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable {
                                        navController.navigateUp()
                                    },
                                contentDescription = null,
                                contentScale = ContentScale.Inside,
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(BackgroundLevel3),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Row {
                                Image(
                                    painter = painterResource(R.drawable.calendar_search),
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {
                                            viewModel.setShowDateFilterDialog(true)
                                        },
                                    contentDescription = null,
                                    contentScale = ContentScale.Inside,
                                    colorFilter = ColorFilter.tint(Color.White)
                                )
                                Spacer(modifier = Modifier.width(15.dp))
                                Image(
                                    painter = painterResource(R.drawable.search),
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {},
                                    contentDescription = null,
                                    contentScale = ContentScale.Inside,
                                    colorFilter = ColorFilter.tint(Color.White)
                                )
                                Spacer(modifier = Modifier.width(15.dp))
                                Image(
                                    painter = painterResource(R.drawable.notification),
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {
                                            viewModel.toggleNotifications {}
                                        },
                                    contentDescription = null,
                                    contentScale = ContentScale.Inside,
                                    colorFilter = ColorFilter.tint(
                                        if (areNotificationsEnabled) Color.Green else Color.White
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(17.dp))
                    Text(
                        modifier = Modifier.padding(bottom = 20.dp),
                        text = stringResource(R.string.categories),
                        color = Color.White,
                        fontSize = 20.sp
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(
                            count = categoriesWithCounts.size
                        ) { index ->
                            val categoryItem = categoriesWithCounts[index]
                            Box(
                                modifier = Modifier
                                    .width((((screenWidth.value - 60) / 2) - 15).dp)
                                    .background(

                                        color = BackgroundLevel2,
                                        shape = RoundedCornerShape(15.dp)
                                    )
                                    .clickable {
                                        viewModel.setCategorySelectedTaskScreen(categoryItem.category.name)
                                        viewModel.setCategorySelectedIdTaskScreen(categoryItem.category.id)
                                        viewModel.setShowCategoryBottomSheet(true)
                                    }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(15.dp)
                                ) {
                                    Text(
                                        modifier = Modifier.padding(start = 6.dp),
                                        text = "${categoryItem.taskCount} ${stringResource(R.string.tasks)}",
                                        fontSize = 10.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        modifier = Modifier.padding(start = 6.dp),
                                        text = categoryItem.category.name,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.horizontal_line),
                                            modifier = Modifier
                                                .height(25.dp)
                                                .weight(1f),
                                            contentDescription = null,
                                            contentScale = ContentScale.FillWidth,
                                            colorFilter = ColorFilter.tint(Color(categoryItem.category.color))
                                        )
                                        Image(
                                            painter = painterResource(R.drawable.horizontal_line),
                                            modifier = Modifier
                                                .height(25.dp)
                                                .weight(1f),
                                            contentDescription = null,
                                            contentScale = ContentScale.FillWidth,
                                            colorFilter = ColorFilter.tint(LinesCategories)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Spacer(modifier = Modifier.weight(1f))
                    LazyColumn {
                        item {
                            EmptyTasksContent(
                                addingTaskClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
        if (drawersTasksScreenState.showDateFilterDialog) {
            DatePickerDialog(
                onDismissRequest = {
                    viewModel.setShowDateFilterDialog(false)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.setShowDateFilterDialog(false)
                            val result = datePickerState.selectedDateMillis
                            if (result != null) {
                                val newCalendar = Calendar.getInstance()
                                    .apply { timeInMillis = result }
                                viewModel.setTaskDate(
                                    day = newCalendar.get(Calendar.DAY_OF_MONTH) + 1,
                                    month = newCalendar.get(Calendar.MONTH) + 1,
                                    year = newCalendar.get(Calendar.YEAR)
                                )
                            }
                            viewModel.setShowDateBottomSheet(true)
                        }
                    ) {
                        Text(text = stringResource(R.string.confirm))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            viewModel.setShowDateFilterDialog(false)
                        }
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }
                },
                colors = DatePickerDefaults.colors()
            ) {
                DatePicker(datePickerState)
            }
        }
        if (drawersTasksScreenState.showDateBottomSheet) {
            val screenHeightPx = with(density) {
                LocalContext.current.resources.displayMetrics.heightPixels.toFloat()
            }
            val calculatedMaxHeightDp: Dp? = drawersTasksScreenState.titlePositionY?.let { titleYPx ->
                val availableHeightPx = screenHeightPx - titleYPx
                if (availableHeightPx > 0) {
                    with(density) { availableHeightPx.toDp() }
                } else {
                    400.dp
                }
            }
            ModalBottomSheet(
                onDismissRequest = {
                    viewModel.setShowDateBottomSheet(false)
                },
                sheetState = sheetState,
                scrimColor = Color.Transparent,
                dragHandle = {
                    DateDragHandle(
                        day = datesState.day,
                        month = datesState.month,
                        year = datesState.year,
                        onClickClosing = {
                            viewModel.setShowDateBottomSheet(false)
                        }
                    )
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BackgroundLevel3)
                        .then(
                            if (calculatedMaxHeightDp != null) {
                                Modifier.height(calculatedMaxHeightDp)
                            } else {
                                Modifier.height(400.dp)
                            }
                        )
                ) {
                    FilterByDate(
                        date = datePickerState.selectedDateMillis
                    )
                }
            }
        }
        if (drawersTasksScreenState.showCategoryBottomSheet && currentCategoryId != null) {
            val screenHeightPx = with(density) {
                LocalContext.current.resources.displayMetrics.heightPixels.toFloat()
            }
            val calculatedMaxHeightDp: Dp? = drawersTasksScreenState.titlePositionY?.let { titleYPx ->
                val availableHeightPx = screenHeightPx - titleYPx
                if (availableHeightPx > 0) {
                    with(density) { availableHeightPx.toDp() }
                } else {
                    400.dp
                }
            }
            ModalBottomSheet(
                onDismissRequest = {
                    viewModel.setShowCategoryBottomSheet(false)
                                   },
                sheetState = sheetState,
                scrimColor = Color.Transparent,
                dragHandle = {
                    CategoryDragHandle(
                        categoryName = drawersTasksScreenState.categorySelectedTaskScreen,
                        onClickClosing = {
                            viewModel.setShowCategoryBottomSheet(false)
                        }
                    )
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BackgroundLevel3)
                        .then(
                            if (calculatedMaxHeightDp != null) {
                                Modifier.height(calculatedMaxHeightDp)
                            } else {
                                Modifier.height(400.dp)
                            }
                        )
                ) {
                    FilterByCategory(
                        idCategory = currentCategoryId
                    )
                }
            }
        }
    }
}