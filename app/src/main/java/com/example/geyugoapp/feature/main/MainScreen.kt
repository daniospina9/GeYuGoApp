package com.example.geyugoapp.feature.main

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.geyugoapp.R
import com.example.geyugoapp.feature.main.composable.HorizontalLine
import com.example.geyugoapp.feature.main.composable.ModalDrawer
import com.example.geyugoapp.ui.theme.BackgroundMainScreen
import com.example.geyugoapp.ui.theme.FramePhotoProfile
import com.example.geyugoapp.ui.theme.LogosMainScreen
import com.example.geyugoapp.ui.util.getFirstWord
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val userName by viewModel.userName.collectAsStateWithLifecycle()

    val simpleUserName = getFirstWord(userName)

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = context) {
        viewModel.events.collect { event ->
            when (event) {
                is MainViewModel.Event.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold { innerPadding ->
        ModalDrawer(
            modifier = Modifier.padding(innerPadding),
            drawerState = drawerState,
            userName = userName
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundMainScreen)
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.55f)
                ) {
                    Image(
                        painter = painterResource(R.drawable.menu_duo),
                        modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                scope.launch {
                                    drawerState.open()
                                }
                            },
                        contentDescription = "A menu to deploy options",
                        contentScale = ContentScale.Inside,
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Spacer(modifier = Modifier.weight(0.2f))
                    Box(
                        modifier = Modifier
                            .padding(start = 24.dp)
                            .size(130.dp)
                            .background(
                                color = LogosMainScreen,
                                shape = CircleShape
                            )
                            .border(
                                width = 7.dp,
                                color = FramePhotoProfile,
                                shape = CircleShape
                            )
                    ) {}
                    Spacer(modifier = Modifier.weight(0.2f))
                    Text(
                        modifier = Modifier.padding(start = 24.dp),
                        text = "Hello",
                        color = Color.White,
                        fontSize = 45.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        modifier = Modifier.padding(start = 24.dp),
                        text = "$simpleUserName",
                        color = Color.White,
                        fontSize = 45.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.weight(0.2f))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BackgroundMainScreen,
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.todo_list),
                                modifier = Modifier.size(20.dp),
                                contentDescription = "Logo about ToDo list",
                                contentScale = ContentScale.Inside,
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                            Spacer(modifier = Modifier.width(7.dp))
                            Text(
                                text = "Your ToDo's",
                                color = Color.White,
                                fontSize = 16.sp,
                            )
                        }
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BackgroundMainScreen,
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.note_edit),
                                modifier = Modifier.size(20.dp),
                                contentDescription = "Logo about Add a new task",
                                contentScale = ContentScale.Inside,
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                            Spacer(modifier = Modifier.width(7.dp))
                            Text(
                                text = "Add Task",
                                color = Color.White,
                                fontSize = 16.sp,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(0.4f))
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.45f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.weight(0.45f))
                    Image(
                        painter = painterResource(R.drawable.achievement),
                        modifier = Modifier.size(80.dp),
                        contentDescription = "Logo with a person getting goals",
                        contentScale = ContentScale.Inside,
                        colorFilter = ColorFilter.tint(LogosMainScreen)
                    )
                    HorizontalLine(
                        width = 40.dp
                    )
                    HorizontalLine(
                        width = 70.dp
                    )
                    HorizontalLine(
                        width = 100.dp
                    )
                    Text(
                        text = "Today is nice",
                        color = LogosMainScreen,
                        fontSize = 16.sp,
                    )
                    Text(
                        text = "to reach",
                        color = LogosMainScreen,
                        fontSize = 16.sp,
                    )
                    Text(
                        text = "your goals",
                        color = LogosMainScreen,
                        fontSize = 16.sp,
                    )
                    HorizontalLine(
                        width = 100.dp
                    )
                    HorizontalLine(
                        width = 70.dp
                    )
                    HorizontalLine(
                        width = 40.dp
                    )
                    Spacer(modifier = Modifier.weight(0.55f))
                }
            }
        }
    }
}