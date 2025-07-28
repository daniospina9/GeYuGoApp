package com.example.geyugoapp.feature.main.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.geyugoapp.R
import com.example.geyugoapp.navigation.categories.CategoriesRoute
import com.example.geyugoapp.ui.theme.BackgroundLevel1
import com.example.geyugoapp.ui.theme.BackgroundLevel3
import com.example.geyugoapp.ui.theme.MenuSeparator
import com.example.geyugoapp.ui.theme.UnselectedMenuBackground

@Composable
fun ModalDrawer(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    userName: String?,
    navController: NavController,
    userId: Long?,
    content: @Composable () -> Unit

) {
    val navigationDrawerWidth = 260.dp

    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(navigationDrawerWidth),
                drawerContainerColor = Color(BackgroundLevel3)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp)
                        .height(120.dp)
                        .background(Color(BackgroundLevel3)),
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
                                .clickable {},
                            contentDescription = "A menu to deploy options",
                            contentScale = ContentScale.Inside,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
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
                        navController.navigate(
                            CategoriesRoute(
                                userId = userId
                            )
                        )
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
                            modifier = Modifier.weight(0.7f),
                            text = "Your Categories",
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Image(
                            painter = painterResource(R.drawable.category_variety),
                            modifier = Modifier
                                .weight(0.3f)
                                .size(40.dp),
                            contentDescription = "A menu to deploy options",
                            contentScale = ContentScale.Inside,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                }
                Button(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .width((navigationDrawerWidth.value * 0.8).dp),
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(BackgroundLevel1),
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(0.7f),
                            text = "Settings",
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Image(
                            painter = painterResource(R.drawable.settings),
                            modifier = Modifier
                                .weight(0.3f)
                                .size(40.dp),
                            contentDescription = "A menu to deploy options",
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