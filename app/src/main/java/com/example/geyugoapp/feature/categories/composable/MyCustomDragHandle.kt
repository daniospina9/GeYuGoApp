package com.example.geyugoapp.feature.categories.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.geyugoapp.ui.theme.ModalBottomSeparator

@Composable
fun MyCustomDragHandle(
    modifier: Modifier = Modifier,
    width: Dp,
    height: Dp = 6.dp,
    shapeColor: Color = Color(ModalBottomSeparator)
) {
    Surface(
        modifier = modifier,
        color = Color(ModalBottomSeparator),
    ) {
        Box(
            modifier = Modifier
                .width(width)
                .height(height)
                .background(
                    color = shapeColor,
                    shape = RectangleShape
                )
        )
    }
}