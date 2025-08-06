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
import androidx.compose.ui.res.stringResource
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
import com.example.geyugoapp.ui.theme.BackgroundLevel1
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
                            userId = event.userId,
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
                .background(Color(BackgroundLevel1))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.45f))
            Text(
                text = stringResource(R.string.geyugo),
                color = Color.White,
                fontSize = 70.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                modifier = Modifier.padding(top = 3.dp),
                text = stringResource(R.string.your_personal_to_do_app),
                color = Color.White,
                fontSize = 19.sp
            )
            Spacer(modifier = Modifier.weight(0.2f))
            Text(
                modifier = Modifier.padding(bottom = 6.dp),
                text = stringResource(R.string.what_s_your_name),
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
                        text = stringResource(R.string.enter_your_name_here),
                        color = Color(FirstUserTFT),
                        fontSize = 23.sp,
                        textAlign = TextAlign.Center
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(BackgroundLevel1),
                    unfocusedContainerColor = Color(BackgroundLevel1),
                    disabledContainerColor = Color(BackgroundLevel1),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color(FirstUserLTF),
                    focusedIndicatorColor = Color(FirstUserLTF),
                    unfocusedIndicatorColor = Color(FirstUserLTF),
                )
            )
            Spacer(modifier = Modifier.weight(0.4f))
            Button(
                modifier = Modifier.size(width = 65.dp, height = 70.dp),
                onClick = { viewModel.saveUser() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(FirstUserButton),
                )
            ) {
                Image(
                    painter = painterResource(R.drawable.arrow_right),
                    modifier = Modifier.size(40.dp),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }
    }
}