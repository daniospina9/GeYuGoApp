package com.example.geyugoapp.feature.tasks

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.geyugoapp.R
import com.example.geyugoapp.feature.tasks.composable.ModalDrawerTasks
import com.example.geyugoapp.ui.theme.BackgroundLevel2
import com.example.geyugoapp.ui.theme.BackgroundLevel3
import com.example.geyugoapp.ui.theme.CreateButtons
import com.example.geyugoapp.ui.theme.FramePhotoProfile
import com.example.geyugoapp.ui.theme.LinesCategories
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun TasksScreen(
    viewModel: TasksViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val categoriesByUser by viewModel.categoriesByUser.collectAsStateWithLifecycle()

    val tasksByUserId by viewModel.tasksByUserId.collectAsStateWithLifecycle()

    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

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
            Column (
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
                            contentDescription = "A back logo to come back to previous screen",
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
                        Image(
                            painter = painterResource(R.drawable.notification),
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {},
                            contentDescription = "A notification logo to activate/deactivate notifications",
                            contentScale = ContentScale.Inside,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(17.dp))
                Text(
                    modifier = Modifier.padding(bottom = 20.dp),
                    text = "CATEGORIES",
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
                                .width(((screenWidth.value - 60) / 2).dp)
                                .background(

                                    color = Color(BackgroundLevel2),
                                    shape = RoundedCornerShape(15.dp)
                                )
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
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.horizontal_line),
                                        modifier = Modifier
                                            .height(25.dp)
                                            .weight(1f),
                                        contentDescription = "A line with category color",
                                        contentScale = ContentScale.FillWidth,
                                        colorFilter = ColorFilter.tint(Color(categoryItem.color))
                                    )
                                    Image(
                                        painter = painterResource(R.drawable.horizontal_line),
                                        modifier = Modifier
                                            .height(25.dp)
                                            .weight(1f),
                                        contentDescription = "A line with category color",
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
                    modifier = Modifier.padding(bottom = 20.dp),
                    text = "TODAY'S TASKS",
                    color = Color.White,
                    fontSize = 17.sp
                )
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        count = tasksByUserId.size
                    ) {
                        val matchingCategory = categoriesByUser.find { category ->
                            category.id == tasksByUserId[it].categoryId
                        }
                        val colorBox = matchingCategory?.color ?: FramePhotoProfile
                        val taskDateTimeMillis = tasksByUserId[it].dateTime
                        val calendar = Calendar.getInstance()
                        calendar.timeInMillis = taskDateTimeMillis
                        val hour = calendar.get(Calendar.HOUR_OF_DAY)
                        val minute = calendar.get(Calendar.MINUTE)
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
                                    .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(23.dp)
                                        .background(
                                            color = Color(BackgroundLevel2),
                                            shape = CircleShape
                                        )
                                        .border(
                                            width = 2.dp,
                                            color = Color(colorBox),
                                            shape = CircleShape
                                        )
                                )
                                Spacer(modifier = Modifier.width(15.dp))
                                Text(
                                    modifier = Modifier.weight(0.7f),
                                    text = tasksByUserId[it].name,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = Color.White,
                                    fontSize = 15.sp
                                )
                                Text(
                                    modifier = Modifier.weight(0.1f),
                                    text = "$hour:$minute",
                                    color = Color.White,
                                    fontSize = 10.sp
                                )
                            }
                        }

                    }
                }
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
                        contentDescription = "A add circle image",
                        contentScale = ContentScale.Inside,
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }
        }

    }
}