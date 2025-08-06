package com.example.geyugoapp.feature.tasks

import android.widget.Toast
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
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.geyugoapp.R
import com.example.geyugoapp.feature.tasks.composable.CategoryDragHandle
import com.example.geyugoapp.feature.tasks.composable.DateDragHandle
import com.example.geyugoapp.feature.tasks.composable.FilterByCategory
import com.example.geyugoapp.feature.tasks.composable.FilterByDate
import com.example.geyugoapp.feature.tasks.composable.ModalDrawerTasks
import com.example.geyugoapp.feature.tasks.composable.TasksList
import com.example.geyugoapp.ui.theme.BackgroundLevel2
import com.example.geyugoapp.ui.theme.BackgroundLevel3
import com.example.geyugoapp.ui.theme.CreateButtons
import com.example.geyugoapp.ui.theme.LinesCategories
import com.example.geyugoapp.ui.util.tasks.filterByDateCategory
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    viewModel: TasksViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val density = LocalDensity.current

    val categoriesByUser by viewModel.categoriesByUser.collectAsStateWithLifecycle()

    val tasksByUserId by viewModel.tasksByUserId.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    var showDateDialog by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = calendar.timeInMillis,
        initialDisplayMode = DisplayMode.Input
    )

    var showDateBottomSheet by remember { mutableStateOf(false) }

    var showCategoryBottomSheet by remember { mutableStateOf(false) }

    var allowSheetClose by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { targetValue ->
            if (targetValue == SheetValue.Hidden) {
                val canClose = allowSheetClose
                if (canClose) {
                    allowSheetClose = false
                }
                canClose
            } else {
                true
            }
        }
    )

    var titlePositionY by remember { mutableStateOf<Float?>(null) }

    val tasksForDay = filterByDateCategory(
        date = datePickerState.selectedDateMillis,
        tasksByUserId = tasksByUserId
    )

    var day by rememberSaveable { mutableIntStateOf(calendar.get(android.icu.util.Calendar.DAY_OF_MONTH)) }

    var month by rememberSaveable { mutableIntStateOf(calendar.get(android.icu.util.Calendar.MONTH) + 1) }

    var year by rememberSaveable { mutableIntStateOf(calendar.get(android.icu.util.Calendar.YEAR)) }

    var categorySelected by rememberSaveable { mutableStateOf("") }

    var categorySelectedId by remember { mutableStateOf<Long?>(null) }

    val currentCategoryId = categorySelectedId

    LaunchedEffect(key1 = context) {
        viewModel.events.collect { event ->
            when (event) {
                is TasksViewModel.Event.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold { innerPadding ->
        ModalDrawerTasks(
            modifier = Modifier.padding(innerPadding),
            drawerState = drawerState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(BackgroundLevel3))
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(BackgroundLevel3))
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(BackgroundLevel3)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Image(
                            painter = painterResource(R.drawable.back),
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {},
                            contentDescription = null,
                            contentScale = ContentScale.Inside,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(BackgroundLevel3)),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Row {
                            Image(
                                painter = painterResource(R.drawable.calendar_search),
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable {
                                        showDateDialog = true
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
                                    .clickable {},
                                contentDescription = null,
                                contentScale = ContentScale.Inside,
                                colorFilter = ColorFilter.tint(Color.White)
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
                        count = categoriesByUser.size
                    ) { index ->
                        val categoryItem = categoriesByUser[index]
                        Box(
                            modifier = Modifier
                                .width((((screenWidth.value - 60) / 2) - 15).dp)
                                .background(

                                    color = Color(BackgroundLevel2),
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .clickable {
                                    categorySelected = categoryItem.name
                                    categorySelectedId = categoryItem.id
                                    showCategoryBottomSheet = true
                                }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp)
                            ) {
                                Text(
                                    modifier = Modifier.padding(start = 6.dp),
                                    text = "40 tasks",
                                    fontSize = 10.sp,
                                    color = Color.White
                                )
                                Text(
                                    modifier = Modifier.padding(start = 6.dp),
                                    text = categoryItem.name,
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
                                        colorFilter = ColorFilter.tint(Color(categoryItem.color))
                                    )
                                    Image(
                                        painter = painterResource(R.drawable.horizontal_line),
                                        modifier = Modifier
                                            .height(25.dp)
                                            .weight(1f),
                                        contentDescription = null,
                                        contentScale = ContentScale.FillWidth,
                                        colorFilter = ColorFilter.tint(Color(LinesCategories))
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
                            titlePositionY = positionInRoot.y
                        },
                    text = stringResource(R.string.today_s_tasks),
                    color = Color.White,
                    fontSize = 17.sp
                )
                TasksList(
                    tasksForDay = tasksForDay,
                    categoriesByUser = categoriesByUser,
                    startPadding = 0.dp,
                    endPadding = 0.dp,
                    bottomPadding = 0.dp,
                    backgroundColor = BackgroundLevel3
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
                            color = Color(CreateButtons),
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
        if (showDateDialog) {
            DatePickerDialog(
                onDismissRequest = {
                    showDateDialog = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDateDialog = false
                            val result = datePickerState.selectedDateMillis
                            if (result != null) {
                                val newCalendar = android.icu.util.Calendar.getInstance()
                                    .apply { timeInMillis = result }
                                day = newCalendar.get(android.icu.util.Calendar.DAY_OF_MONTH) + 1
                                month = newCalendar.get(android.icu.util.Calendar.MONTH) + 1
                                year = newCalendar.get(android.icu.util.Calendar.YEAR)
                            }
                            showDateBottomSheet = true
                        }
                    ) {
                        Text(text = stringResource(R.string.confirm))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDateDialog = false
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
        if (showDateBottomSheet) {
            val screenHeightPx = with(density) {
                LocalContext.current.resources.displayMetrics.heightPixels.toFloat()
            }
            val calculatedMaxHeightDp: Dp? = titlePositionY?.let { titleYPx ->
                val availableHeightPx = screenHeightPx - titleYPx
                if (availableHeightPx > 0) {
                    with(density) { availableHeightPx.toDp() - 35.dp }
                } else {
                    400.dp
                }
            }
            ModalBottomSheet(
                onDismissRequest = { showDateBottomSheet = false },
                sheetState = sheetState,
                scrimColor = Color.Transparent,
                dragHandle = {
                    DateDragHandle(
                        day = day,
                        month = month,
                        year = year,
                        onClickClosing = {
                            showDateBottomSheet = false
                        }
                    )
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(BackgroundLevel3))
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
        if (showCategoryBottomSheet && currentCategoryId != null) {
            val screenHeightPx = with(density) {
                LocalContext.current.resources.displayMetrics.heightPixels.toFloat()
            }
            val calculatedMaxHeightDp: Dp? = titlePositionY?.let { titleYPx ->
                val availableHeightPx = screenHeightPx - titleYPx
                if (availableHeightPx > 0) {
                    with(density) { availableHeightPx.toDp() - 35.dp }
                } else {
                    400.dp
                }
            }
            ModalBottomSheet(
                onDismissRequest = { showCategoryBottomSheet = false },
                sheetState = sheetState,
                scrimColor = Color.Transparent,
                dragHandle = {
                    CategoryDragHandle(
                        categoryName = categorySelected,
                        onClickClosing = {
                            showCategoryBottomSheet = false
                        }
                    )
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(BackgroundLevel3))
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