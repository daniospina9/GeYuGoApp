package com.example.geyugoapp.feature.categories

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geyugoapp.domain.categories.models.Category
import com.example.geyugoapp.domain.categories.usecases.DeleteCategory
import com.example.geyugoapp.domain.categories.usecases.GetCategoriesByUserId
import com.example.geyugoapp.domain.categories.usecases.InsertCategory
import com.example.geyugoapp.domain.categories.usecases.UpdateCategory
import com.example.geyugoapp.domain.notifications.usecases.ObserveNotificationSettingsByUserId
import com.example.geyugoapp.domain.notifications.usecases.ToggleNotifications
import com.example.geyugoapp.domain.task.usecases.GetCountTasksByCategory
import com.example.geyugoapp.ui.theme.ColorCategoryOthers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NewCategoryState(
    val newCategory: String = "",
    val color: Long = ColorCategoryOthers.toArgb().toLong(),
    val colorsExpanded: Boolean = false,
    val selectionColor: Long = 0L
)

data class CategoryWithTaskCount(
    val category: Category,
    val taskCount: Int
)

data class CategoriesScreenStates(
    val showBottomSheetByOption: Int = 0,
    val dropdownMenuVisibleForItem: Int? = null,
    val categoryByEdit: Category? = null,
    val currentCategoryName: String = "",
    val currentCategoryColor: Long = 0L,
    val showDialog: Boolean = false
)

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val insertCategory: InsertCategory,
    private val getCategoriesByUserId: GetCategoriesByUserId,
    private val updateCategory: UpdateCategory,
    private val deleteCategory: DeleteCategory,
    private val getCountTasksByCategory: GetCountTasksByCategory,
    private val toggleNotifications: ToggleNotifications,
    private val observeNotificationSettings: ObserveNotificationSettingsByUserId
) : ViewModel() {

    private val _modalCategoriesBottomState = MutableStateFlow(NewCategoryState())
    val modalCategoriesBottomState = _modalCategoriesBottomState.asStateFlow()

    private val _categoriesByUser = MutableStateFlow<List<Category>>(emptyList())

    private val _categoriesWithCounts = MutableStateFlow<List<CategoryWithTaskCount>>(emptyList())
    val categoriesWithCounts = _categoriesWithCounts.asStateFlow()

    private val _categoriesScreenStates = MutableStateFlow(CategoriesScreenStates())
    val categoriesScreenStates = _categoriesScreenStates.asStateFlow()

    private val _areNotificationsEnabled = MutableStateFlow(false)
    val areNotificationsEnabled = _areNotificationsEnabled.asStateFlow()

    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    val currentUserId = savedStateHandle.get<Long?>("userId")

    init {
        refreshCategories()
        observeNotificationSettings()
    }

    private fun observeNotificationSettings() {
        viewModelScope.launch {
            currentUserId?.let { userId ->
                observeNotificationSettings(userId).collect { settings ->
                    _areNotificationsEnabled.update { settings?.areNotificationsEnabled ?: false }
                }
            }
        }
    }

    fun toggleNotifications(onPermissionNeeded: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            currentUserId?.let { userId ->
                val currentState = _areNotificationsEnabled.value

                // Si estamos activando las notificaciones, necesitamos verificar permisos
                if (!currentState) {
                    _events.send(Event.RequestNotificationPermission)
                    return@launch
                }

                // Si estamos desactivando, proceder directamente
                val result = toggleNotifications.invoke(userId, false)

                result.onSuccess { settings ->
                    _areNotificationsEnabled.update { settings.areNotificationsEnabled }
                    _events.send(Event.ShowMessage("Notifications disabled for all tasks"))
                }.onFailure { error ->
                    _events.send(Event.ShowMessage("Error toggling notifications: ${error.message}"))
                }
            } ?: run {
                _events.send(Event.ShowMessage("Error: User ID not found"))
            }
        }
    }

    fun enableNotificationsAfterPermission() {
        viewModelScope.launch(Dispatchers.IO) {
            currentUserId?.let { userId ->
                val result = toggleNotifications.invoke(userId, true)

                result.onSuccess { settings ->
                    _areNotificationsEnabled.update { settings.areNotificationsEnabled }
                    _events.send(Event.ShowMessage("Notifications enabled for all tasks"))
                }.onFailure { error ->
                    _events.send(Event.ShowMessage("Error enabling notifications: ${error.message}"))
                }
            }
        }
    }

    private fun refreshCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            if (currentUserId != null) {
                _categoriesByUser.update{  getCategoriesByUserId(userId = currentUserId) }
                val categories = _categoriesByUser.value
                val categoriesWithCountsList = categories.map { category ->
                    val count = getCountTasksByCategory.invoke(category.id)
                    CategoryWithTaskCount(category = category, taskCount = count)
                }
                _categoriesWithCounts.update { categoriesWithCountsList }
            }
            return@launch
        }
    }
    fun setNewCategory(newCategory: String) {
        _modalCategoriesBottomState.update { it.copy(newCategory = newCategory) }
    }

    fun setColor(color: Long) {
        _modalCategoriesBottomState.update { it.copy(color = color) }
    }

    fun setEditingCategory(newCategory: String) {
        _modalCategoriesBottomState.update { it.copy(newCategory = newCategory) }
    }

    fun setShowBottomSheetByOption(showBottomSheetByOption: Int) {
        _categoriesScreenStates.update { it.copy(showBottomSheetByOption = showBottomSheetByOption) }
    }

    fun setDropdownMenuVisibleForItem(dropdownMenuVisibleForItem: Int?) {
        _categoriesScreenStates.update { it.copy(dropdownMenuVisibleForItem = dropdownMenuVisibleForItem) }
    }

    fun setCategoryByEdit(categoryByEdit: Category) {
        _categoriesScreenStates.update { it.copy(categoryByEdit = categoryByEdit) }
    }

    fun setCurrentCategoryName(currentCategoryName: String) {
        _categoriesScreenStates.update { it.copy(currentCategoryName = currentCategoryName) }
    }

    fun setCurrentCategoryColor(currentCategoryColor: Long) {
        _categoriesScreenStates.update { it.copy(currentCategoryColor = currentCategoryColor) }
    }

    fun setShowDialog(showDialog: Boolean) {
        _categoriesScreenStates.update { it.copy(showDialog = showDialog) }
    }

    fun setColorsExpanded(colorsExpanded: Boolean) {
        _modalCategoriesBottomState.update { it.copy(colorsExpanded = colorsExpanded) }
    }

    fun setSelectionColor(selectionColor: Long) {
        _modalCategoriesBottomState.update { it.copy(selectionColor = selectionColor) }
    }

    fun insertCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            insertCategory.invoke(category)
            _events.send(Event.ShowMessage("Category added successfully"))
            refreshCategories()
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteCategory.invoke(category)
            _events.send(Event.ShowMessage("Category deleted successfully"))
            refreshCategories()
        }
    }

    fun updateExistCategory(
        category: Category,
        name: String,
        color: Long
    ) {
        val myNewCategory = _modalCategoriesBottomState.value.newCategory
        val newColor = _modalCategoriesBottomState.value.color
        viewModelScope.launch(Dispatchers.IO) {
            if ((myNewCategory.isBlank() && color == newColor)
                || (myNewCategory == name && color == newColor)) {
                _events.send(Event.ShowMessage("Nothing changed"))
                return@launch
            } else if (myNewCategory.isBlank() && color != newColor) {
                val updateCategoryDbDto = category.copy(name = name, color = newColor)
                updateCategory(updateCategoryDbDto)
                _events.send(Event.ShowMessage("Category modified successfully"))
                refreshCategories()
                return@launch
            }
            val updateCategoryDbDto = category.copy(name = myNewCategory, color = newColor)
            updateCategory(updateCategoryDbDto)
            _modalCategoriesBottomState.update { it.copy(newCategory = "") }
            _events.send(Event.ShowMessage("Category modified successfully"))
            refreshCategories()
        }
    }

    fun addCategory() {
        val myNewCategory = _modalCategoriesBottomState.value.newCategory
        val newColor = _modalCategoriesBottomState.value.color
        viewModelScope.launch(Dispatchers.IO) {
            if (myNewCategory.isBlank()) {
                _events.send(Event.ShowMessage("Category name cannot be empty"))
                return@launch
            } else if (currentUserId != null) {
                insertCategory(
                    Category(
                        name = myNewCategory,
                        userId = currentUserId,
                        color = newColor
                    )
                )
                _modalCategoriesBottomState.update { it.copy(newCategory = "") }
            } else {
                _events.send(Event.ShowMessage("Error: Cannot save category. User ID is missing"))
                _modalCategoriesBottomState.update { it.copy(newCategory = "") }
            }

        }
    }

    sealed class Event {
        data class ShowMessage(val message: String) : Event()
        object RequestNotificationPermission : Event()
    }
}