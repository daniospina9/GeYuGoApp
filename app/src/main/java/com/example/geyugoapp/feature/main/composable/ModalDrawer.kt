package com.example.geyugoapp.feature.main.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.geyugoapp.R
import com.example.geyugoapp.feature.main.MainViewModel
import com.example.geyugoapp.navigation.categories.CategoriesRoute
import com.example.geyugoapp.navigation.main.MainRoute
import com.example.geyugoapp.navigation.settings.SettingsRoute
import com.example.geyugoapp.ui.theme.BackgroundLevel1
import com.example.geyugoapp.ui.theme.BackgroundLevel3
import com.example.geyugoapp.ui.theme.BackgroundUsersDropMenu
import com.example.geyugoapp.ui.theme.ColorUsersDropMenu
import com.example.geyugoapp.ui.theme.MenuSeparator
import com.example.geyugoapp.ui.theme.UnselectedMenuBackground
import com.example.geyugoapp.ui.utils.capitalizeFirstLetter

@Composable
fun ModalDrawer(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    drawerState: DrawerState,
    userName: String?,
    navController: NavController,
    closeDrawer: () -> Unit,
    userId: Long?,
    content: @Composable () -> Unit

) {
    val navigationDrawerWidth = 260.dp

    val expanded by viewModel.expanded.collectAsStateWithLifecycle()
    val users by viewModel.users.collectAsStateWithLifecycle()

    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(navigationDrawerWidth),
                drawerContainerColor = BackgroundLevel3
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp)
                        .height(120.dp)
                        .background(BackgroundLevel3),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "$userName",
                            color = Color.White,
                            fontSize = 28.sp,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Image(
                            painter = painterResource(R.drawable.arrow_down),
                            modifier = Modifier
                                .weight(1f)
                                .size(50.dp)
                                .clickable {
                                    viewModel.setExpanded(expanded = true)
                                },
                            contentDescription = null,
                            contentScale = ContentScale.Inside,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                    DropdownMenu(
                        modifier = Modifier.background(BackgroundUsersDropMenu),
                        expanded = expanded.expanded,
                        onDismissRequest = {
                            viewModel.setExpanded(expanded = false)
                        },
                        offset = DpOffset(21.dp, 0.dp)
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
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.height(6.dp),
                    color = MenuSeparator
                )
                Button(
                    modifier = Modifier
                        .padding(20.dp)
                        .width((navigationDrawerWidth.value * 0.8).dp),
                    onClick = {
                        closeDrawer()
                        navController.navigate(
                            CategoriesRoute(
                                userId = userId
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BackgroundLevel1,
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(0.7f),
                            text = stringResource(R.string.your_categories_),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Image(
                            painter = painterResource(R.drawable.category_variety),
                            modifier = Modifier
                                .weight(0.3f)
                                .size(40.dp),
                            contentDescription = null,
                            contentScale = ContentScale.Inside,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                }
                Button(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .width((navigationDrawerWidth.value * 0.8).dp),
                    onClick = {
                        closeDrawer()
                        navController.navigate(
                            SettingsRoute(
                                userId = userId
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BackgroundLevel1,
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(0.7f),
                            text = stringResource(R.string.settings),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Image(
                            painter = painterResource(R.drawable.settings),
                            modifier = Modifier
                                .weight(0.3f)
                                .size(40.dp),
                            contentDescription = null,
                            contentScale = ContentScale.Inside,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                }
            }
        },
        scrimColor = UnselectedMenuBackground.copy(alpha = 0.8f)
    ) {
        content()
    }

}