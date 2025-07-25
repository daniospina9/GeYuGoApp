@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.geyugoapp.feature.categories

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.geyugoapp.R
import com.example.geyugoapp.domain.categories.models.Category
import com.example.geyugoapp.feature.categories.composable.EmptyCategoriesContent
import com.example.geyugoapp.feature.categories.composable.ModalBottomContent
import com.example.geyugoapp.feature.categories.composable.MyCustomDragHandle
import com.example.geyugoapp.ui.theme.BackgroundLevel2
import com.example.geyugoapp.ui.theme.BackgroundLevel3
import com.example.geyugoapp.ui.theme.ColorCategoryOthers
import com.example.geyugoapp.ui.theme.CreateButtons
import com.example.geyugoapp.ui.theme.FirstUserButton
import com.example.geyugoapp.ui.theme.LinesCategories

@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val categoriesByUser by viewModel.categoriesByUser.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = context) {
        viewModel.events.collect { event ->
            when (event) {
                is CategoriesViewModel.Event.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var showBottomSheetByOption by remember { mutableStateOf<Int?>(null) }

    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp

    val screenWidth = configuration.screenWidthDp.dp

    var dropdownMenuVisibleForItem by remember { mutableStateOf<Int?>(null) }

    var categoryByEdit by remember { mutableStateOf<Category?>(null) }

    val currentCategoryByEdit = categoryByEdit

    var currentCategoryName by remember { mutableStateOf("") }

    var currentCategoryColor by remember { mutableLongStateOf(0L) }

    var showDialog by remember { mutableStateOf(false) }

    Scaffold { innerPadding ->
        if (categoriesByUser.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(BackgroundLevel3)
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.weight(0.9f)
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
                    Spacer(modifier = Modifier.height(17.dp))
                    Text(
                        modifier = Modifier.padding(bottom = 20.dp),
                        text = "YOUR CATEGORIES",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            count = categoriesByUser.size
                        ) { index ->
                            val categoryItem = categoriesByUser[index]
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = BackgroundLevel2,
                                        shape = RoundedCornerShape(15.dp)
                                    )
                            ) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onLongPress = {
                                                    dropdownMenuVisibleForItem = index
                                                },
                                                onTap = {
                                                    Toast.makeText(
                                                        context,
                                                        "Tap",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            )
                                        }
                                        .padding(15.dp)
                                ) {
                                    Text(
                                        modifier = Modifier.padding(start = 6.dp),
                                        text = "40 tasks",
                                        fontSize = 10.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        modifier = Modifier.padding(start = 6.dp),
                                        text = categoryItem.name,
                                        color = Color.White
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.horizontal_line),
                                            modifier = Modifier
                                                .height(25.dp)
                                                .weight(1f),
                                            contentDescription = "A line with category color",
                                            contentScale = ContentScale.FillWidth,
                                            colorFilter = ColorFilter.tint(Color(categoryItem.color))
                                        )
                                        Image(
                                            painter = painterResource(R.drawable.horizontal_line),
                                            modifier = Modifier
                                                .height(25.dp)
                                                .weight(1f),
                                            contentDescription = "A line with category color",
                                            contentScale = ContentScale.FillWidth,
                                            colorFilter = ColorFilter.tint(Color(LinesCategories))
                                        )
                                    }
                                    if (dropdownMenuVisibleForItem == index) {
                                        DropdownMenu(
                                            expanded = (dropdownMenuVisibleForItem == index),
                                            onDismissRequest = {
                                                dropdownMenuVisibleForItem = null
                                            },
                                            offset = DpOffset(3.dp, 0.dp)
                                        ) {
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        text = "Edit"
                                                    )
                                                },
                                                onClick = {
                                                    dropdownMenuVisibleForItem = null
                                                    showBottomSheetByOption = 2
                                                    categoryByEdit = categoryItem
                                                    currentCategoryName = categoryItem.name
                                                    currentCategoryColor = categoryItem.color
                                                }
                                            )
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        text = "Delete"
                                                    )
                                                },
                                                onClick = {
                                                    dropdownMenuVisibleForItem = null
                                                    categoryByEdit = categoryItem
                                                    showDialog = true
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.1f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier
                            .height(55.dp)
                            .width(175.dp),
                        onClick = {
                            showBottomSheetByOption = 1
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(CreateButtons),
                        )
                    ) {
                        Text(
                            text = "New Category",
                            fontSize = 17.sp
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(BackgroundLevel3)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EmptyCategoriesContent(
                    modifier = Modifier.weight(0.93f)
                )
                Button(
                    modifier = Modifier
                        .height(55.dp)
                        .width(175.dp)
                        .weight(0.07f),
                    onClick = {
                        showBottomSheetByOption = 1
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(CreateButtons),
                    )
                ) {
                    Text(
                        text = "New Category",
                        fontSize = 17.sp
                    )
                }
            }
        }
        if (showBottomSheetByOption == 1) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheetByOption = null
                },
                sheetState = sheetState,
                dragHandle = {
                    MyCustomDragHandle(
                        width = screenWidth
                    )
                }
            ) {
                ModalBottomContent(
                    modifier = Modifier.heightIn(max = screenHeight / 2),
                    viewModel = viewModel,
                    label = "Name Category",
                    textColorButton = "Choice Color",
                    initialColor = ColorCategoryOthers,
                    addingButtonText = "Add Category",
                    onClickAdding = {
                        viewModel.addCategory()
                        showBottomSheetByOption = null
                    }
                )
            }
        }
        if (showBottomSheetByOption == 2 && currentCategoryByEdit != null) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheetByOption = null
                },
                sheetState = sheetState,
                dragHandle = {
                    MyCustomDragHandle(
                        width = screenWidth
                    )
                }
            ) {
                ModalBottomContent(
                    modifier = Modifier.heightIn(max = screenHeight / 2),
                    viewModel = viewModel,
                    label = "New Name Category",
                    textColorButton = "Change Color",
                    initialColor = currentCategoryColor,
                    addingButtonText = "Change Category",
                    onClickAdding = {
                        viewModel.updateExistCategory(
                            category = currentCategoryByEdit,
                            name = currentCategoryName,
                            color = currentCategoryColor
                        )
                        showBottomSheetByOption = null
                    }
                )
            }
        }
        if (showDialog && currentCategoryByEdit != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteCategory(currentCategoryByEdit)
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FirstUserButton,
                        )
                    ) {
                        Text(
                            text = "Confirm"
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                        )
                    ) {
                        Text(
                            text = "Abort",
                            color = FirstUserButton
                        )
                    }
                },
                text = {
                    Text(
                        text = "The category selected still have some tasks, if you delete it, it's tasks will delete too. Do you want to continue?",
                        textAlign = TextAlign.Left
                    )
                },
                title = {
                    Text(
                        text = "Alert of elimination",
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
    }
}