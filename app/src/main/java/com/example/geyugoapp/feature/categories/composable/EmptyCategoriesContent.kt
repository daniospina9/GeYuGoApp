package com.example.geyugoapp.feature.categories.composable

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geyugoapp.R
import com.example.geyugoapp.ui.theme.BackgroundLevel3

@Composable
fun EmptyCategoriesContent(modifier: Modifier = Modifier) {
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
                        .clickable {},
                    contentDescription = "A back logo to come back to previous screen",
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
        Spacer(modifier = Modifier.weight(0.4f))
        Image(
            painter = painterResource(R.drawable.bed_hostel_hotel_svgrepo_com),
            modifier = Modifier
                .size(200.dp),
            contentDescription = "A notification logo to activate/deactivate notifications",
            contentScale = ContentScale.Inside,
            colorFilter = ColorFilter.tint(Color.White)
        )
        Text(
            text = "Don't any categories yet?",
            color = Color.White,
            fontSize = 25.sp
        )
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = "Create your first category",
            color = Color.White,
            fontSize = 25.sp
        )
        Image(
            painter = painterResource(R.drawable.curve_arrow_down_svgrepo_com),
            modifier = Modifier
                .size(60.dp),
            contentDescription = "A back logo to come back to previous screen",
            contentScale = ContentScale.Inside,
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}