package com.example.geyugoapp.feature.main.composable

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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.geyugoapp.feature.main.MainViewModel
import com.example.geyugoapp.feature.tasks.TasksViewModel
import com.example.geyugoapp.ui.theme.BackgroundLevel1
import com.example.geyugoapp.ui.theme.BackgroundLevel2
import com.example.geyugoapp.ui.theme.BackgroundLevel3
import com.example.geyugoapp.ui.theme.CreateButtons
import com.example.geyugoapp.ui.theme.UnselectedMenuBackground
import com.example.geyugoapp.ui.utils.tasks.getFormattedTime
import kotlinx.coroutines.launch

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AddTaskModalDrawer(
//    modifier: Modifier = Modifier,
//    drawerState: DrawerState,
//    viewModel: MainViewModel = hiltViewModel(),
//    content: @Composable () -> Unit
//) {
//    val configuration = LocalConfiguration.current
//    val screenWidth = configuration.screenWidthDp.dp
//    val navigationDrawerWidth = screenWidth.value * 0.85
//
//    val state by viewModel.state.collectAsStateWithLifecycle()
//
//    val categoriesByUser by viewModel.categoriesByUser.collectAsStateWithLifecycle()
//
//    val drawerTaskState by viewModel.drawerTaskState.collectAsStateWithLifecycle()
//
//    val datesState by viewModel.datesState.collectAsStateWithLifecycle()
//
//    val scope = rememberCoroutineScope()
//
//    val datePickerState = rememberDatePickerState(
//        initialSelectedDateMillis = datesState.calendar.timeInMillis,
//        initialDisplayMode = DisplayMode.Input
//    )
//
//    val timePickerState = rememberTimePickerState(
//        is24Hour = false
//    )
//
//    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
//        ModalNavigationDrawer(
//            modifier = modifier,
//            drawerState = drawerState,
//            drawerContent = {
//                ModalDrawerSheet(
//                    modifier = Modifier.width(navigationDrawerWidth.dp),
//                    drawerContainerColor = Color(BackgroundLevel3)
//                ) {
//                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
//                        Column(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .padding(24.dp)
//                        ) {
//                            Row(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalArrangement = Arrangement.Start
//                            ) {
//                                Image(
//                                    painter = painterResource(R.drawable.back),
//                                    modifier = Modifier
//                                        .size(30.dp)
//                                        .clickable {
//                                            scope.launch { drawerState.close() }
//                                        },
//                                    contentDescription = null,
//                                    contentScale = ContentScale.Inside,
//                                    colorFilter = ColorFilter.tint(Color.White)
//                                )
//                            }
//                            Spacer(modifier = Modifier.weight(0.4f))
//                            TextField(
//                                modifier = Modifier.padding(start = 24.dp),
//                                value = state.name,
//                                onValueChange = { name ->
//                                    viewModel.setName(name)
//                                },
//                                label = {
//                                    Text(
//                                        text = stringResource(R.string.add_your_task),
//                                        color = Color.White
//                                    )
//                                },
//                                colors = TextFieldDefaults.colors(
//                                    focusedContainerColor = Color(BackgroundLevel2),
//                                    unfocusedContainerColor = Color(BackgroundLevel2),
//                                    disabledContainerColor = Color(BackgroundLevel2),
//                                    focusedTextColor = Color.White,
//                                    unfocusedTextColor = Color.White,
//                                    cursorColor = Color.White,
//                                    focusedIndicatorColor = Color(BackgroundLevel2),
//                                    unfocusedIndicatorColor = Color(BackgroundLevel2),
//                                )
//                            )
//                            Spacer(modifier = Modifier.weight(0.23f))
//                            Box(
//                                modifier = Modifier.padding(start = 24.dp)
//                            ) {
//                                Button(
//                                    modifier = Modifier
//                                        .height(50.dp)
//                                        .width(155.dp),
//                                    onClick = {
//                                        viewModel.setExpandedTaskMenu(true)
//                                    },
//                                    colors = ButtonDefaults.buttonColors(
//                                        containerColor = Color(BackgroundLevel1),
//                                    ),
//                                    shape = RoundedCornerShape(16.dp)
//                                ) {
//                                    Row(
//                                        verticalAlignment = Alignment.CenterVertically
//                                    ) {
//                                        Text(
//                                            text = drawerTaskState.categorySelection,
//                                            color = Color.White,
//                                            fontSize = 14.sp,
//                                        )
//                                        Spacer(modifier = Modifier.width(4.dp))
//                                        Image(
//                                            painter = painterResource(R.drawable.arrow_down),
//                                            modifier = Modifier
//                                                .size(45.dp),
//                                            contentDescription = null,
//                                            contentScale = ContentScale.Inside,
//                                            colorFilter = ColorFilter.tint(Color.White)
//                                        )
//                                    }
//                                }
//                                DropdownMenu(
//                                    expanded = drawerTaskState.expandedTaskMenu,
//                                    onDismissRequest = {
//                                        viewModel.setExpandedTaskMenu(false)
//                                    },
//                                    offset = DpOffset(21.dp, 0.dp)
//                                ) {
//                                    if (categoriesByUser.isEmpty()) {
//                                        Text(
//                                            modifier = Modifier
//                                                .padding(start = 6.dp, end = 6.dp)
//                                                .clickable {
//                                                    viewModel.setExpandedTaskMenu(false)
//                                                },
//                                            text = stringResource(R.string.there_are_no_categories),
//                                        )
//                                    } else {
//                                        categoriesByUser.forEach { category ->
//                                            DropdownMenuItem(
//                                                text = { Text(category.name) },
//                                                onClick = {
//                                                    viewModel.setExpandedTaskMenu(false)
//                                                    viewModel.setCategorySelection(category.name)
//                                                }
//                                            )
//                                        }
//                                    }
//                                }
//                            }
//                            Box(
//                                modifier = Modifier.padding(start = 24.dp, top = 18.dp)
//                            ) {
//                                Button(
//                                    modifier = Modifier
//                                        .height(50.dp)
//                                        .width(155.dp),
//                                    onClick = {
//                                        viewModel.setShowDateTaskMenuDialog(true)
//                                    },
//                                    colors = ButtonDefaults.buttonColors(
//                                        containerColor = Color(BackgroundLevel1),
//                                    ),
//                                    shape = RoundedCornerShape(16.dp)
//                                ) {
//                                    Row(
//                                        verticalAlignment = Alignment.CenterVertically
//                                    ) {
//                                        Image(
//                                            painter = painterResource(R.drawable.calendar_search),
//                                            modifier = Modifier
//                                                .size(30.dp),
//                                            contentDescription = null,
//                                            contentScale = ContentScale.Inside,
//                                            colorFilter = ColorFilter.tint(Color.White)
//                                        )
//                                        Spacer(modifier = Modifier.width(4.dp))
//                                        Text(
//                                            text = "${datesState.day}/${datesState.month}/${datesState.year}",
//                                            color = Color.White,
//                                            fontSize = 14.sp,
//                                        )
//                                    }
//                                }
//                            }
//                            Box(
//                                modifier = Modifier.padding(start = 24.dp, top = 18.dp)
//                            ) {
//                                Button(
//                                    modifier = Modifier
//                                        .height(50.dp)
//                                        .width(155.dp),
//                                    onClick = {
//                                        viewModel.setShowTimePicker(true)
//                                    },
//                                    colors = ButtonDefaults.buttonColors(
//                                        containerColor = Color(BackgroundLevel1),
//                                    ),
//                                    shape = RoundedCornerShape(16.dp)
//                                ) {
//                                    Row(
//                                        verticalAlignment = Alignment.CenterVertically
//                                    ) {
//                                        Text(
//                                            text = drawerTaskState.timeTaskMessage,
//                                            color = Color.White,
//                                            fontSize = 14.sp,
//                                        )
//                                    }
//                                }
//                            }
//                            Spacer(modifier = Modifier.weight(0.37f))
//                            Button(
//                                modifier = Modifier
//                                    .padding(start = 24.dp)
//                                    .height(65.dp)
//                                    .width(200.dp),
//                                onClick = {
//                                    scope.launch { drawerState.close() }
//                                    val hourToUse: Int
//                                    val minuteToUse: Int
//                                    val name: String
//                                    if (drawerTaskState.timeTaskMessage == "Add Time") {
//                                        hourToUse = 23
//                                        minuteToUse = 59
//                                    } else {
//                                        hourToUse = timePickerState.hour
//                                        minuteToUse = timePickerState.minute
//                                    }
//                                    if (drawerTaskState.categorySelection == "Category") {
//                                        viewModel.createOthersCategory(
//                                            selectedDateMillis = datePickerState.selectedDateMillis,
//                                            hour = hourToUse,
//                                            minute = minuteToUse
//                                        )
//                                    } else {
//                                        name = drawerTaskState.categorySelection
//                                        viewModel.addTask(
//                                            selectedDateMillis = datePickerState.selectedDateMillis,
//                                            hour = hourToUse,
//                                            minute = minuteToUse,
//                                            name = name
//                                        )
//                                    }
//                                    viewModel.setTimeTaskMessage("Add Time")
//                                    viewModel.setCategorySelection("Category")
//                                },
//                                colors = ButtonDefaults.buttonColors(
//                                    containerColor = Color(CreateButtons),
//                                )
//                            ) {
//                                Text(
//                                    text = stringResource(R.string.create_task),
//                                    fontSize = 22.sp
//                                )
//                            }
//                        }
//                    }
//                }
//            },
//            scrimColor = Color(UnselectedMenuBackground).copy(alpha = 0.8f)
//        )
//        {
//            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
//                content()
//            }
//        }
//    }
//    if (drawerTaskState.showDateTaskMenuDialog) {
//        DatePickerDialog(
//            onDismissRequest = {
//                viewModel.setShowDateTaskMenuDialog(false)
//            },
//            confirmButton = {
//                TextButton(
//                    onClick = {
//                        viewModel.setShowDateTaskMenuDialog(false)
//                        val result = datePickerState.selectedDateMillis
//                        if (result != null) {
//                            val newCalendar = Calendar.getInstance().apply { timeInMillis = result }
//                            viewModel.setTaskDate(
//                                day = newCalendar.get(Calendar.DAY_OF_MONTH) + 1,
//                                month = newCalendar.get(Calendar.MONTH) + 1,
//                                year = newCalendar.get(Calendar.YEAR)
//                            )
//                        }
//                    }
//                ) {
//                    Text(text = stringResource(R.string.confirm))
//                }
//            },
//            colors = DatePickerDefaults.colors()
//        ) {
//            DatePicker(datePickerState)
//        }
//    }
//    if (drawerTaskState.showTimePicker) {
//        Dialog(
//            onDismissRequest = {
//                viewModel.setShowTimePicker(false)
//            }
//        ) {
//            Column(
//                modifier = Modifier
//                    .background(
//                        color = Color.White,
//                        shape = RoundedCornerShape(30.dp)
//                    )
//                    .padding(13.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Start
//                ) {
//                    Text(
//                        text = stringResource(R.string.select_time),
//                        color = Color(BackgroundLevel3),
//                        fontSize = 12.sp
//                    )
//                }
//                Spacer(modifier = Modifier.height(9.dp))
//                TimePicker(
//                    timePickerState,
//                    layoutType = TimePickerLayoutType.Vertical,
//                    colors = TimePickerDefaults.colors(
//                        clockDialColor = Color(BackgroundLevel3),
//                        clockDialSelectedContentColor = Color(BackgroundLevel3),
//                        selectorColor = Color.White,
//                        clockDialUnselectedContentColor = Color.White,
//                        containerColor = Color.White,
//                        periodSelectorUnselectedContentColor = Color(BackgroundLevel3),
//                        periodSelectorUnselectedContainerColor = Color.White,
//                        periodSelectorSelectedContentColor = Color.White,
//                        periodSelectorSelectedContainerColor = Color(BackgroundLevel3),
//                        timeSelectorUnselectedContentColor = Color(BackgroundLevel3),
//                        timeSelectorUnselectedContainerColor = Color.White,
//                        timeSelectorSelectedContentColor = Color.White,
//                        timeSelectorSelectedContainerColor = Color(BackgroundLevel3)
//                    )
//                )
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.End,
//                    verticalAlignment = Alignment.Bottom
//                ) {
//                    Button(
//                        onClick = {
//                            viewModel.setShowTimePicker(false)
//                        },
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Color.White
//                        )
//                    ) {
//                        Text(
//                            text = stringResource(R.string.cancel),
//                            color = Color(BackgroundLevel3)
//                        )
//                    }
//                    Button(
//                        onClick = {
//                            viewModel.setShowTimePicker(false)
//                            val transformedTime = getFormattedTime(
//                                hour24 = timePickerState.hour,
//                                minute = timePickerState.minute
//                            )
//                            val formattedTime = transformedTime.formattedTime
//                            val amPm = transformedTime.amPm
//                            viewModel.setTimeTaskMessage("$formattedTime $amPm")
//                        },
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Color.White
//                        )
//                    ) {
//                        Text(
//                            text = stringResource(R.string.ok),
//                            color = Color(BackgroundLevel3)
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
