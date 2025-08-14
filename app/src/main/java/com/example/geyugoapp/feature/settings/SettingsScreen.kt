package com.example.geyugoapp.feature.settings

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.geyugoapp.R
import com.example.geyugoapp.navigation.firstuser.FirstUserRoute
import com.example.geyugoapp.navigation.main.MainRoute
import com.example.geyugoapp.ui.theme.BackgroundLevel1
import com.example.geyugoapp.ui.theme.BackgroundLevel2
import com.example.geyugoapp.ui.theme.BackgroundUsersDropMenu
import com.example.geyugoapp.ui.theme.ColorUsersDropMenu
import com.example.geyugoapp.ui.theme.DeleteButtonColor
import com.example.geyugoapp.ui.theme.FirstUserButton
import com.example.geyugoapp.ui.utils.capitalizeFirstLetter

@Composable
fun SettingScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val drawersState by viewModel.drawersState.collectAsStateWithLifecycle()
    val userName by viewModel.userName.collectAsStateWithLifecycle()
    val users by viewModel.users.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = context) {
        viewModel.event.collect { event ->
            when (event) {
                is SettingsViewModel.Event.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is SettingsViewModel.Event.NavigateToMain -> {
                    navController.navigate(
                        MainRoute(
                            userId = event.userId
                        )
                    )
                }
                is SettingsViewModel.Event.NavigateToFirstUser -> {
                    navController.navigate(
                        FirstUserRoute
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
                .background(BackgroundLevel1)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f),
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
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.settings),
                        color = Color.White,
                        fontSize = 27.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Box(
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .width((screenWidth.value * 0.8).dp),
                    onClick = {
                        viewModel.setExpanded(
                            expanded = true
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BackgroundLevel2,
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.users),
                            modifier = Modifier
                                .size(60.dp),
                            contentDescription = null,
                            contentScale = ContentScale.Inside,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = capitalizeFirstLetter(userName),
                            color = Color.White,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Image(
                            painter = painterResource(R.drawable.arrow_down),
                            modifier = Modifier
                                .size(60.dp),
                            contentDescription = null,
                            contentScale = ContentScale.Inside,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                }
                DropdownMenu(
                    modifier = Modifier.background(BackgroundUsersDropMenu),
                    expanded = drawersState.expanded,
                    onDismissRequest = {
                        viewModel.setExpanded(expanded = false)
                    },
                    offset = DpOffset((screenWidth.value/4).dp, 0.dp)
                ) {
                    users.forEach { user ->
                        DropdownMenuItem(
                            contentPadding = PaddingValues(0.dp),
                            text = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 2.dp,
                                            top = 1.dp,
                                            bottom = 7.dp,
                                            end = 2.dp
                                        )
                                        .background(
                                            color = ColorUsersDropMenu,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .width(150.dp)
                                        .height(40.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.user),
                                            modifier = Modifier
                                                .padding(start = 15.dp)
                                                .size(18.dp),
                                            contentDescription = null,
                                            contentScale = ContentScale.Inside,
                                            colorFilter = ColorFilter.tint(Color.White)
                                        )
                                        Spacer(modifier = Modifier.width(7.dp))
                                        Text(
                                            text = capitalizeFirstLetter(user.name),
                                            color = Color.White,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            },
                            onClick = {
                                navController.navigate(
                                    MainRoute(
                                        userId = user.id
                                    )
                                )
                                viewModel.setExpanded(expanded = false)
                            }
                        )
                    }
                    DropdownMenuItem(
                        contentPadding = PaddingValues(0.dp),
                        text = {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 2.dp,
                                        top = 1.dp,
                                        bottom = 7.dp,
                                        end = 2.dp
                                    )
                                    .background(
                                        color = ColorUsersDropMenu,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .width(150.dp)
                                    .height(40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.add),
                                        modifier = Modifier
                                            .size(30.dp),
                                        contentDescription = null,
                                        contentScale = ContentScale.Inside,
                                        colorFilter = ColorFilter.tint(Color.White)
                                    )
                                    Spacer(modifier = Modifier.width(7.dp))
                                    Text(
                                        text = stringResource(R.string.add_user),
                                        color = Color.White,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        },
                        onClick = {
                            viewModel.setNewUserDialog(true)
                            viewModel.setExpanded(expanded = false)
                        }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .width((screenWidth.value * 0.8).dp),
                    onClick = {
                        viewModel.setShowDialog(true)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeleteButtonColor,
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.delete),
                            modifier = Modifier
                                .size(60.dp),
                            contentDescription = null,
                            contentScale = ContentScale.Inside,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            text = stringResource(R.string.delete_my_user),
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
        if(drawersState.showDialog) {
            AlertDialog(
                onDismissRequest = {
                    viewModel.setShowDialog(false)
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val userToEliminate = users.find {user ->
                                user.name == userName
                            }
                            if (userToEliminate != null) {
                                viewModel.eliminateUser(userToEliminate)
                            }
                            viewModel.setShowDialog(false)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FirstUserButton,
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.delete)
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            viewModel.setShowDialog(false)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.abort),
                            color = FirstUserButton
                        )
                    }
                },
                text = {
                    Text(
                        text = stringResource(R.string.if_you_decided_eliminate_your_user_every_categories_and_task_associated_with_it_will_eliminate_too_besides_you_won_t_be_able_to_recover_this_user_do_you_want_to_continue),
                        textAlign = TextAlign.Left
                    )
                },
                title = {
                    Text(
                        text = stringResource(R.string.alert_of_elimination),
                    )
                },
                shape = RoundedCornerShape(15.dp),
                containerColor = Color.White,
                textContentColor = Color.Black,
                tonalElevation = 12.dp,
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    securePolicy = SecureFlagPolicy.Inherit,
                    usePlatformDefaultWidth = true,
                    decorFitsSystemWindows = true
                )
            )
        }
        if (drawersState.newUserDialog) {
            Dialog(
                onDismissRequest = {
                    viewModel.setNewUserDialog(false)
                }
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    TextField(
                        label = { Text(
                            text = stringResource(R.string.add_your_new_user)
                        ) },
                        value = drawersState.newUser,
                        onValueChange = {newUser ->
                            viewModel.setNewUser(newUser)
                        }
                    )
                    Row(
                    ) {
                        Button(
                            onClick = {
                                viewModel.setNewUserDialog(false)
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.come_back)
                            )
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        Button(
                            onClick = {
                                viewModel.insertNewUser()
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.create)
                            )
                        }
                    }
                }
            }
        }
    }
}