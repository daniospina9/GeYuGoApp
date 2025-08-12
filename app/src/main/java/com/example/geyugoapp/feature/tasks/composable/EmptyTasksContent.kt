package com.example.geyugoapp.feature.tasks.composable

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import com.example.geyugoapp.ui.theme.CreateButtons

@Composable
fun EmptyTasksContent(
    modifier: Modifier = Modifier,
    addingTaskClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            text = "Any task Today To Do?",
            color = Color.White,
            fontSize = 25.sp
        )
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = "Come On, Add",
            color = Color.White,
            fontSize = 25.sp
        )
        Text(
            text = "your first task",
            color = Color.White,
            fontSize = 25.sp
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(BackgroundLevel3)),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row {
                    Image(
                        painter = painterResource(R.drawable.curve_arrow_right),
                        modifier = Modifier
                            .size(60.dp),
                        contentDescription = null,
                        contentScale = ContentScale.Inside,
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(
                                color = Color(CreateButtons),
                                shape = CircleShape
                            )
                            .clickable {
                                addingTaskClick()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier.size(45.dp),
                            painter = painterResource(R.drawable.add),
                            contentDescription = null,
                            contentScale = ContentScale.Inside,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                }
            }
        }
    }
}