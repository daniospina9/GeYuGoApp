package com.example.geyugoapp.feature.firstuser

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.geyugoapp.R
import com.example.geyugoapp.navigation.main.MainRoute
import com.example.geyugoapp.ui.theme.BackgroundFirstUser
import com.example.geyugoapp.ui.theme.FirstUserButton
import com.example.geyugoapp.ui.theme.FirstUserLTF
import com.example.geyugoapp.ui.theme.FirstUserTFT

@Composable
fun FirstUserScreen(
    navController: NavController,
    viewModel: FirstUserViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = context) {
        viewModel.events.collect { event ->
            when(event) {
                is FirstUserViewModel.Event.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is FirstUserViewModel.Event.NavigateToMain -> {
                    navController.navigate(
                        MainRoute(
                            userId = ,
                            name = state.name
                        )
                    )
                }
            }
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(BackgroundFirstUser)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.45f))
            Text(
                text = "GeYuGo",
                color = Color.White,
                fontSize = 70.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                modifier = Modifier.padding(top = 3.dp),
                text = "Your Personal To-Do App",
                color = Color.White,
                fontSize = 19.sp
            )
            Spacer(modifier = Modifier.weight(0.2f))
            Text(
                modifier = Modifier.padding(bottom = 6.dp),
                text = "What's your name?",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )
            TextField(
                value = state.name,
                onValueChange = { name ->
                    viewModel.setName(name)
                },
                textStyle = TextStyle(
                    fontSize = 23.sp,
                    textAlign = TextAlign.Center
                ),
                placeholder = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Enter your name here",
                        color = FirstUserTFT,
                        fontSize = 23.sp,
                        textAlign = TextAlign.Center
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = BackgroundFirstUser,
                    unfocusedContainerColor = BackgroundFirstUser,
                    disabledContainerColor = BackgroundFirstUser,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = FirstUserLTF,
                    focusedIndicatorColor = FirstUserLTF,
                    unfocusedIndicatorColor = FirstUserLTF,
                )
            )
            Spacer(modifier = Modifier.weight(0.4f))
            Button(
                modifier = Modifier.size(width = 65.dp, height = 70.dp),
                onClick = { viewModel.insertUser() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = FirstUserButton,
                )
            ) {
                Image(
                    painter = painterResource(R.drawable.right_arrow),
                    modifier = Modifier.size(40.dp),
                    contentDescription = "A right arrow to continue",
                    contentScale = ContentScale.Inside,
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }
    }
}