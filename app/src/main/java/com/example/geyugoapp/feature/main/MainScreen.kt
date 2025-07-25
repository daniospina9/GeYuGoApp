package com.example.geyugoapp.feature.main

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.geyugoapp.R
import com.example.geyugoapp.feature.main.composable.ModalDrawer
import com.example.geyugoapp.ui.theme.BackgroundLevel1
import com.example.geyugoapp.ui.theme.BackgroundLevel2
import com.example.geyugoapp.ui.theme.BackgroundLevel3
import com.example.geyugoapp.ui.theme.FramePhotoProfile
import com.example.geyugoapp.ui.theme.LogosMainScreen
import com.example.geyugoapp.ui.util.getFirstWord
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val userName by viewModel.userName.collectAsStateWithLifecycle()

    val userId = viewModel.userId

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
            userName = userName,
            userId = userId,
            navController = navController
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundLevel2)
                    .padding(24.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.menu_duo),
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                    contentDescription = "A menu to deploy options",
                    contentScale = ContentScale.Inside,
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Spacer(modifier = Modifier.weight(0.065f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(76.dp)
                            .background(
                                color = BackgroundLevel1,
                                shape = CircleShape
                            )
                            .border(
                                width = 3.dp,
                                color = FramePhotoProfile,
                                shape = CircleShape
                            )
                    ) {}
                    Spacer(modifier = Modifier.width(3.dp))
                    Column {
                        Text(
                            modifier = Modifier.padding(start = 24.dp),
                            text = "Hello",
                            color = Color.White,
                            fontSize = 27.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            modifier = Modifier.padding(start = 24.dp),
                            text = "$simpleUserName",
                            color = Color.White,
                            fontSize = 27.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(0.035f))
                Box(
                    modifier = Modifier
                        .weight(0.9f)
                        .fillMaxSize()
                        .background(
                            color = BackgroundLevel1,
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 10.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Image(
                                painter = painterResource(R.drawable.arrow_trend_up),
                                modifier = Modifier.size(40.dp),
                                contentDescription = "Logo with a trend up arrow",
                                contentScale = ContentScale.Inside,
                                colorFilter = ColorFilter.tint(LogosMainScreen)
                            )
                        }
                        Text(
                            text = "Today is a nice day to reach",
                            color = Color.White,
                            fontSize = 23.sp
                        )
                        Text(
                            text = "your goals",
                            color = Color.White,
                            fontSize = 23.sp
                        )
                        Spacer(modifier = Modifier.weight(0.3f))
                        Box(
                            modifier = Modifier
                                .padding(start = 24.dp)
                                .size(130.dp)
                                .background(
                                    color = BackgroundLevel1,
                                    shape = CircleShape
                                )
                                .border(
                                    width = 7.dp,
                                    color = FramePhotoProfile,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.achievement),
                                modifier = Modifier.size(60.dp),
                                contentDescription = "Logo with a person getting goals",
                                contentScale = ContentScale.Inside,
                                colorFilter = ColorFilter.tint(LogosMainScreen)
                            )
                        }
                        Spacer(modifier = Modifier.weight(0.7f))
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp),
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BackgroundLevel3,
                            ),
                            shape = RoundedCornerShape(10.dp)
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
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp, top = 7.dp, bottom = 20.dp),
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BackgroundLevel3,
                            ),
                            shape = RoundedCornerShape(10.dp)
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
                    }
                }
            }
        }
    }
}
