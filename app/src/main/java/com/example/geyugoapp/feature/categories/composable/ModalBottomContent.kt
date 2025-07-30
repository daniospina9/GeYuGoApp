@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.geyugoapp.feature.categories.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.geyugoapp.R
import com.example.geyugoapp.feature.categories.CategoriesViewModel
import com.example.geyugoapp.ui.theme.BackgroundLevel1
import com.example.geyugoapp.ui.theme.BackgroundLevel2
import com.example.geyugoapp.ui.theme.ColorCategory1
import com.example.geyugoapp.ui.theme.ColorCategory2
import com.example.geyugoapp.ui.theme.ColorCategory3
import com.example.geyugoapp.ui.theme.ColorCategory4
import com.example.geyugoapp.ui.theme.ColorCategory5
import com.example.geyugoapp.ui.theme.ColorCategory6
import com.example.geyugoapp.ui.theme.ColorCategory7
import com.example.geyugoapp.ui.theme.CreateButtons

@Composable
fun ModalBottomContent(
    modifier: Modifier = Modifier,
    viewModel: CategoriesViewModel = hiltViewModel(),
    label: String,
    textColorButton: String,
    initialColor: Long,
    addingButtonText: String,
    onClickAdding: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val focusRequester = remember { FocusRequester() }

    var expanded by remember { mutableStateOf(false) }

    var selection by remember { mutableLongStateOf(initialColor) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(BackgroundLevel1))
            .focusRequester(focusRequester)
            .focusable(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        TextField(
            value = state.newCategory,
            onValueChange = { newCategory ->
                viewModel.setNewCategory(newCategory)
            },
            label = {
                Text(
                    text = label,
                    color = Color.White
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(BackgroundLevel2),
                unfocusedContainerColor = Color(BackgroundLevel2),
                disabledContainerColor = Color(BackgroundLevel2),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = Color(BackgroundLevel2),
                unfocusedIndicatorColor = Color(BackgroundLevel2),
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Box {
            Button(
                onClick = {
                    expanded = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(BackgroundLevel2),
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(15.dp)
                            .background(
                                color = Color(selection),
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(9.dp))
                    Text(
                        text = textColorButton,
                        color = Color.White,
                        fontSize = 16.sp,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Image(
                        painter = painterResource(R.drawable.arrow_up),
                        modifier = Modifier
                            .size(35.dp),
                        contentDescription = "A menu to deploy options",
                        contentScale = ContentScale.Inside,
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {expanded = false},
                offset = DpOffset(45.dp, 0.dp)
            ) {
                DropdownMenuItem(
                    text = {
                        DropdownMenuContent(
                            color = ColorCategory7,
                            text = "Color 7"
                        )
                    },
                    onClick = {
                        expanded = false
                        selection = ColorCategory7
                    })
                DropdownMenuItem(
                    text = {
                        DropdownMenuContent(
                            color = ColorCategory6,
                            text = "Color 6"
                        )
                    },
                    onClick = {
                        expanded = false
                        selection = ColorCategory6
                    })
                DropdownMenuItem(
                    text = {
                        DropdownMenuContent(
                            color = ColorCategory5,
                            text = "Color 5"
                        )
                    },
                    onClick = {
                        expanded = false
                        selection = ColorCategory5
                    })
                DropdownMenuItem(
                    text = {
                        DropdownMenuContent(
                            color = ColorCategory4,
                            text = "Color 4"
                        )
                    },
                    onClick = {
                        expanded = false
                        selection = ColorCategory4
                    })
                DropdownMenuItem(
                    text = {
                        DropdownMenuContent(
                            color = ColorCategory3,
                            text = "Color 3"
                        )
                    },
                    onClick = {
                        expanded = false
                        selection = ColorCategory3
                    })
                DropdownMenuItem(
                    text = {
                        DropdownMenuContent(
                            color = ColorCategory2,
                            text = "Color 2"
                        )
                    },
                    onClick = {
                        expanded = false
                        selection = ColorCategory2
                    })
                DropdownMenuItem(
                    text = {
                        DropdownMenuContent(
                            color = ColorCategory1,
                            text = "Color 1"
                        )
                    },
                    onClick = {
                        expanded = false
                        selection = ColorCategory1
                    })
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                viewModel.setColor(selection)
                onClickAdding()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(CreateButtons),
            )
        ) {
            Text(
                text = addingButtonText,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}