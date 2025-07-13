package com.example.geyugoapp.feature.main.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.geyugoapp.ui.theme.LogosMainScreen


@Composable
fun HorizontalLine(
    modifier: Modifier = Modifier,
    width: Dp,
    color: Color = LogosMainScreen,
    thickness: Dp = 5.dp,
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .width(width = width)
            .height(thickness)
            .background(color)
    )
}