package com.example.geyugoapp.feature.tasks.composable

import android.icu.util.Calendar
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.geyugoapp.R
import com.example.geyugoapp.feature.tasks.TasksViewModel
import com.example.geyugoapp.ui.theme.BackgroundLevel1
import com.example.geyugoapp.ui.theme.BackgroundLevel2
import com.example.geyugoapp.ui.theme.BackgroundLevel3
import com.example.geyugoapp.ui.theme.CreateButtons
import com.example.geyugoapp.ui.theme.UnselectedMenuBackground
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalDrawerTasks(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    viewModel: TasksViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val navigationDrawerWidth = screenWidth.value * 0.85

    val state by viewModel.state.collectAsStateWithLifecycle()

    val categoriesByUser by viewModel.categoriesByUser.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    var expanded by remember { mutableStateOf(false) }

    var selection by remember { mutableStateOf("Category") }

    var showDateDialog by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
    calendar.set(java.util.Calendar.MINUTE, 0)
    calendar.set(java.util.Calendar.SECOND, 0)
    calendar.set(java.util.Calendar.MILLISECOND, 0)

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = calendar.timeInMillis,
        initialDisplayMode = DisplayMode.Input
    )

    var showTimePicker by remember { mutableStateOf(false) }

    val timePickerState = rememberTimePickerState(
        is24Hour = false
    )

    var day by rememberSaveable { mutableIntStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }

    var month by rememberSaveable { mutableIntStateOf(calendar.get(Calendar.MONTH) +1) }

    var year by rememberSaveable { mutableIntStateOf(calendar.get(Calendar.YEAR)) }

    var timeMessage by remember { mutableStateOf("Add Time") }


    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            modifier = modifier,
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.width(navigationDrawerWidth.dp),
                    drawerContainerColor = Color(BackgroundLevel3)
                ) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.back),
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {
                                            scope.launch { drawerState.close() }
                                        },
                                    contentDescription = null,
                                    contentScale = ContentScale.Inside,
                                    colorFilter = ColorFilter.tint(Color.White)
                                )
                            }
                            Spacer(modifier = Modifier.weight(0.4f))
                            TextField(
                                modifier = Modifier.padding(start = 24.dp),
                                value = state.name,
                                onValueChange = { name ->
                                    viewModel.setName(name)
                                },
                                label = {
                                    Text(
                                        text = stringResource(R.string.add_your_task),
                                        color = Color.White
                                    )
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(BackgroundLevel2),
                                    unfocusedContainerColor = Color(BackgroundLevel2),
                                    disabledContainerColor = Color(BackgroundLevel2),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    cursorColor = Color.White,
                                    focusedIndicatorColor = Color(BackgroundLevel2),
                                    unfocusedIndicatorColor = Color(BackgroundLevel2),
                                )
                            )
                            Spacer(modifier = Modifier.weight(0.23f))
                            Box(
                                modifier = Modifier.padding(start = 24.dp)
                            ) {
                                Button(
                                    modifier = Modifier
                                        .height(50.dp)
                                        .width(155.dp),
                                    onClick = {
                                        expanded = true
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(BackgroundLevel1),
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = selection,
                                            color = Color.White,
                                            fontSize = 14.sp,
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Image(
                                            painter = painterResource(R.drawable.arrow_down),
                                            modifier = Modifier
                                                .size(45.dp),
                                            contentDescription = null,
                                            contentScale = ContentScale.Inside,
                                            colorFilter = ColorFilter.tint(Color.White)
                                        )
                                    }
                                }
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    offset = DpOffset(21.dp, 0.dp)
                                ) {
                                    if (categoriesByUser.isEmpty()) {
                                        Text(
                                            modifier = Modifier
                                                .padding(start = 6.dp, end = 6.dp)
                                                .clickable {
                                                    expanded = false
                                                },
                                            text = stringResource(R.string.there_are_no_categories),
                                        )
                                    } else {
                                        categoriesByUser.forEach { category ->
                                            DropdownMenuItem(
                                                text = { Text(category.name) },
                                                onClick = {
                                                    expanded = false
                                                    selection = category.name
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                            Box(
                                modifier = Modifier.padding(start = 24.dp, top = 18.dp)
                            ) {
                                Button(
                                    modifier = Modifier
                                        .height(50.dp)
                                        .width(155.dp),
                                    onClick = {
                                        showDateDialog = true
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(BackgroundLevel1),
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.calendar_search),
                                            modifier = Modifier
                                                .size(30.dp),
                                            contentDescription = null,
                                            contentScale = ContentScale.Inside,
                                            colorFilter = ColorFilter.tint(Color.White)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "$day/$month/$year",
                                            color = Color.White,
                                            fontSize = 14.sp,
                                        )
                                    }
                                }
                            }
                            Box(
                                modifier = Modifier.padding(start = 24.dp, top = 18.dp)
                            ) {
                                Button(
                                    modifier = Modifier
                                        .height(50.dp)
                                        .width(155.dp),
                                    onClick = {
                                        showTimePicker = true
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(BackgroundLevel1),
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = timeMessage,
                                            color = Color.White,
                                            fontSize = 14.sp,
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.weight(0.37f))
                            Button(
                                modifier = Modifier
                                    .padding(start = 24.dp)
                                    .height(65.dp)
                                    .width(200.dp),
                                onClick = {
                                    scope.launch { drawerState.close() }
                                    val hourToUse: Int
                                    val minuteToUse: Int
                                    val name: String
                                    if (timeMessage == "Add Time") {
                                        hourToUse = 23
                                        minuteToUse = 59
                                    } else {
                                        hourToUse = timePickerState.hour
                                        minuteToUse = timePickerState.minute
                                    }
                                    if (selection == "Category") {
                                        viewModel.createOthersCategory(
                                            selectedDateMillis = datePickerState.selectedDateMillis,
                                            hour = hourToUse,
                                            minute = minuteToUse
                                        )
                                    } else {
                                        name = selection
                                        viewModel.addTask(
                                            selectedDateMillis = datePickerState.selectedDateMillis,
                                            hour = hourToUse,
                                            minute = minuteToUse,
                                            name = name
                                        )
                                    }
                                    timeMessage = "Add Time"
                                    selection = "Category"
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(CreateButtons),
                                )
                            ) {
                                Text(
                                    text = stringResource(R.string.create_task),
                                    fontSize = 22.sp
                                )
                            }
                        }
                    }
                }
            },
            scrimColor = Color(UnselectedMenuBackground).copy(alpha = 0.8f)
        )
        {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                content()
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
                            val newCalendar = Calendar.getInstance().apply { timeInMillis = result }
                            day = newCalendar.get(Calendar.DAY_OF_MONTH) + 1
                            month = newCalendar.get(Calendar.MONTH) + 1
                            year = newCalendar.get(Calendar.YEAR)
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.confirm))
                }
            },
            colors = DatePickerDefaults.colors()
        ) {
            DatePicker(datePickerState)
        }
    }
    if (showTimePicker) {
        Dialog(
            onDismissRequest = { showTimePicker = false }
        ) {
            Column(
                modifier = Modifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(13.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = stringResource(R.string.select_time),
                        color = Color(BackgroundLevel3),
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(9.dp))
                TimePicker(
                    timePickerState,
                    layoutType = TimePickerLayoutType.Vertical,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = Color(BackgroundLevel3),
                        clockDialSelectedContentColor = Color(BackgroundLevel3),
                        selectorColor = Color.White,
                        clockDialUnselectedContentColor = Color.White,
                        containerColor = Color.White,
                        periodSelectorUnselectedContentColor = Color(BackgroundLevel3),
                        periodSelectorUnselectedContainerColor = Color.White,
                        periodSelectorSelectedContentColor = Color.White,
                        periodSelectorSelectedContainerColor = Color(BackgroundLevel3),
                        timeSelectorUnselectedContentColor = Color(BackgroundLevel3),
                        timeSelectorUnselectedContainerColor = Color.White,
                        timeSelectorSelectedContentColor = Color.White,
                        timeSelectorSelectedContainerColor = Color(BackgroundLevel3)
                        )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Button(
                        onClick = {
                            showTimePicker = false
                                  },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            color = Color(BackgroundLevel3)
                        )
                    }
                    Button(
                        onClick = {
                            showTimePicker = false
                            val hour24 = timePickerState.hour
                            val minute = timePickerState.minute
                            val hour12 = when {
                                hour24 == 0 -> 12
                                hour24 > 12 -> hour24 - 12
                                else -> hour24
                            }
                            val amPm = if (hour24 < 12) "AM" else "PM"
                            val formattedTime = String.format("%02d:%02d", hour12, minute)
                            timeMessage = "$formattedTime $amPm"
                                  },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.ok),
                            color = Color(BackgroundLevel3)
                        )
                    }
                }
                }
            }
        }
    }
