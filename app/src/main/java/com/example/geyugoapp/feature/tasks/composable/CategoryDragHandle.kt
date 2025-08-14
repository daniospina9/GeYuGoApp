package com.example.geyugoapp.feature.tasks.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.example.geyugoapp.R
import com.example.geyugoapp.ui.theme.BackgroundLevel3
import com.example.geyugoapp.ui.theme.UnselectedMenuBackground

@Composable
fun CategoryDragHandle(
    modifier: Modifier = Modifier,
    categoryName: String,
    onClickClosing: () -> Unit
) {
    Surface(
        modifier = modifier,
        color = BackgroundLevel3
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$categoryName ${stringResource(R.string.tasks_)}",
                color = UnselectedMenuBackground,
                style = TextStyle (
                    fontStyle = FontStyle.Italic
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.close_circle),
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        onClickClosing()
                    },
                contentDescription = null,
                contentScale = ContentScale.Inside,
                colorFilter = ColorFilter.tint(UnselectedMenuBackground)
            )
        }

    }
}