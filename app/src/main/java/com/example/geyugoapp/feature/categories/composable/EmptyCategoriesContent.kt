package com.example.geyugoapp.feature.categories.composable

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.geyugoapp.R
import com.example.geyugoapp.feature.categories.CategoriesViewModel
import com.example.geyugoapp.ui.theme.BackgroundLevel3
import com.example.geyugoapp.ui.theme.UnselectedMenuBackground

@Composable
fun EmptyCategoriesContent(
    modifier: Modifier = Modifier,
    viewModel: CategoriesViewModel = hiltViewModel(),
    backClick: () -> Unit
) {
    val context = LocalContext.current

    val areNotificationsEnabled by viewModel.areNotificationsEnabled.collectAsStateWithLifecycle()

    // Permission launcher for notifications
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.enableNotificationsAfterPermission()
        } else {
            Toast.makeText(context, "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(key1 = context) {
        viewModel.events.collect { event ->
            when (event) {
                is CategoriesViewModel.Event.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is CategoriesViewModel.Event.RequestNotificationPermission -> {
                    // Check if permission is already granted
                    val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    } else {
                        true // Pre-Android 13 doesn't need runtime permission
                    }

                    if (hasPermission) {
                        viewModel.enableNotificationsAfterPermission()
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BackgroundLevel3)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(BackgroundLevel3),
                contentAlignment = Alignment.CenterStart
            ) {
                Image(
                    painter = painterResource(R.drawable.back),
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            backClick()
                        },
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(BackgroundLevel3),
                contentAlignment = Alignment.CenterEnd
            ) {
                Image(
                    painter = painterResource(
                        if (areNotificationsEnabled) R.drawable.notification else R.drawable.notification_off
                    ),
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            viewModel.toggleNotifications {}
                        },
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    colorFilter = ColorFilter.tint(
                        if (areNotificationsEnabled) Color.White else UnselectedMenuBackground
                    )
                )
            }
        }
        Spacer(modifier = Modifier.weight(0.4f))
        Image(
            painter = painterResource(R.drawable.bed_hostel_hotel),
            modifier = Modifier
                .size(200.dp),
            contentDescription = null,
            contentScale = ContentScale.Inside,
            colorFilter = ColorFilter.tint(Color.White)
        )
        Text(
            text = stringResource(R.string.don_t_any_categories_yet),
            color = Color.White,
            fontSize = 25.sp
        )
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = stringResource(R.string.create_your_first_category),
            color = Color.White,
            fontSize = 25.sp
        )
        Image(
            painter = painterResource(R.drawable.curve_arrow_down),
            modifier = Modifier
                .size(60.dp),
            contentDescription = null,
            contentScale = ContentScale.Inside,
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}