package com.example.geyugoapp.feature.tasks

import android.icu.util.Calendar
import android.util.Log
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geyugoapp.domain.categories.models.Category
import com.example.geyugoapp.domain.categories.usecases.GetCategoriesByUserId
import com.example.geyugoapp.domain.categories.usecases.GetCategoryIdByName
import com.example.geyugoapp.domain.categories.usecases.GetCountCategoriesByName
import com.example.geyugoapp.domain.categories.usecases.InsertCategory
import com.example.geyugoapp.domain.task.models.Task
import com.example.geyugoapp.domain.notifications.usecases.ObserveNotificationSettingsByUserIdUseCase
import com.example.geyugoapp.domain.notifications.usecases.ToggleNotificationsUseCase
import com.example.geyugoapp.domain.task.usecases.DeleteTask
import com.example.geyugoapp.domain.task.usecases.GetCountTasksByCategory
import com.example.geyugoapp.domain.task.usecases.GetTasksByUserId
import com.example.geyugoapp.domain.task.usecases.InsertTask
import com.example.geyugoapp.domain.task.usecases.UpdateTask
import com.example.geyugoapp.feature.categories.CategoryWithTaskCount
import com.example.geyugoapp.ui.theme.ColorCategoryOthers
import com.example.geyugoapp.ui.utils.tasks.getCombinedDateTimeMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TasksState(
    val name: String = ""
)

data class CategoryWithTaskCount(
    val category: Category,
    val taskCount: Int
)

data class DrawerTaskState(
    val expandedTaskMenu: Boolean = false,
    val categorySelection: String = "Category",
    val showDateTaskMenuDialog: Boolean = false,
    val showTimePicker: Boolean = false,
    val timeTaskMessage: String = "Add Time"
)

data class TaskDate(
    val day: Int = 0,
    val month: Int = 0,
    val year: Int = 0,
    val calendar: Calendar = Calendar.getInstance()
)

@HiltViewModel
class TasksViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCategoriesByUserId: GetCategoriesByUserId,
    private val insertTask: InsertTask,
    private val getCategoryIdByName: GetCategoryIdByName,
    private val getTasksByUserId: GetTasksByUserId,
    private val insertCategory: InsertCategory,
    private val getCountCategoriesByName: GetCountCategoriesByName,
    private val deleteTask: DeleteTask,
    private val updateTask: UpdateTask,
    private val getCountTasksByCategory: GetCountTasksByCategory,
    private val toggleNotifications: ToggleNotificationsUseCase,
    private val observeNotificationSettings: ObserveNotificationSettingsByUserIdUseCase
) : ViewModel() {

    private val _categoriesByUser = MutableStateFlow<List<Category>>(emptyList())
    val categoriesByUser = _categoriesByUser.asStateFlow()

    private val _categoriesWithCounts = MutableStateFlow<List<CategoryWithTaskCount>>(emptyList())
    val categoriesWithCounts = _categoriesWithCounts.asStateFlow()

    private val _drawerTaskState = MutableStateFlow(DrawerTaskState())
    val drawerTaskState = _drawerTaskState.asStateFlow()

    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(TasksState())
    val state = _state.asStateFlow()

    private val _tasksByUserId = MutableStateFlow<List<Task>>(emptyList())
    val tasksByUserId = _tasksByUserId.asStateFlow()

    private val _datesState = MutableStateFlow(TaskDate())
    val datesState = _datesState.asStateFlow()

    private val _areNotificationsEnabled = MutableStateFlow(false)
    val areNotificationsEnabled = _areNotificationsEnabled.asStateFlow()

    val userId = savedStateHandle.get<Long?>("userId")

    init {
        refreshCategories()
        refreshTasks()
        observeNotificationSettings()
        val calendar = Calendar.getInstance()
        // NO setear hora/minuto a 0 - mantener la hora actual para fechas por defecto
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        _datesState.update {
            it.copy(
                day = calendar.get(Calendar.DAY_OF_MONTH),
                month = calendar.get(Calendar.MONTH) + 1,
                year = calendar.get(Calendar.YEAR),
                calendar = calendar
            )
        }

    }

    fun setExpandedTaskMenu(expandedTaskMenu: Boolean) {
        _drawerTaskState.update { it.copy(expandedTaskMenu = expandedTaskMenu) }
    }

    fun setCategorySelection(categorySelection: String) {
        _drawerTaskState.update { it.copy(categorySelection = categorySelection) }
    }

    fun setShowDateTaskMenuDialog(showDateTaskMenuDialog: Boolean) {
        _drawerTaskState.update { it.copy(showDateTaskMenuDialog = showDateTaskMenuDialog) }
    }

    fun setShowTimePicker(showTimePicker: Boolean) {
        _drawerTaskState.update { it.copy(showTimePicker = showTimePicker) }
    }

    fun setTaskDate(day: Int, month: Int, year: Int) {
        _datesState.update { it.copy(day = day, month = month, year = year) }
    }

    fun setTimeTaskMessage(timeTaskMessage: String) {
        _drawerTaskState.update { it.copy(timeTaskMessage = timeTaskMessage) }
    }

    private fun refreshCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            if (userId != null) {
                _categoriesByUser.update { getCategoriesByUserId(userId = userId) }
                val categories = _categoriesByUser.value
                val categoriesWithCountsList = categories.map { category ->
                    val count = getCountTasksByCategory.invoke(category.id)
                    CategoryWithTaskCount(category = category, taskCount = count)
                }
                _categoriesWithCounts.update { categoriesWithCountsList }
            } else {
                _events.send(Event.ShowMessage("Error: Could not load categories. User ID is missing or invalid"))
            }
        }
    }

    fun refreshTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            if (userId != null) {
                _tasksByUserId.update { getTasksByUserId(userId = userId) }
            } else {
                _events.send(Event.ShowMessage("Could not load tasks"))
            }
        }
    }

    fun setName(name: String) {
        _state.update { it.copy(name = name) }
    }

    fun removeTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteTask(task)
            refreshTasks()
            refreshCategories()
        }
    }

    fun updateCurrentTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            val updateTaskDbDto = task.copy(isClicked = !task.isClicked)
            updateTask(updateTaskDbDto)
            refreshTasks()
        }
    }

    fun addTask(selectedDateMillis: Long?, hour: Int, minute: Int, name: String) {
        val myNewTask = _state.value.name
        val dateTimeByUI = getCombinedDateTimeMillis(
            selectedDateMillis = selectedDateMillis,
            hour = hour,
            minute = minute
        )
        val dateTime = dateTimeByUI ?: 0L
        viewModelScope.launch(Dispatchers.IO) {
            if (myNewTask.isBlank()) {
                _events.send(Event.ShowMessage("Task name cannot be empty"))
                return@launch
            } else if (userId != null) {
                val categoryId = getCategoryIdByName(name = name, userId = userId).id
                insertTask.invoke(
                    Task(
                        name = myNewTask,
                        dateTime = dateTime,
                        userId = userId,
                        categoryId = categoryId
                    )
                )
                refreshTasks()
                refreshCategories()
                _state.update { it.copy(name = "") }
            } else {
                _events.send(Event.ShowMessage("Error: Cannot save task. User ID is missing"))
                _state.update { it.copy(name = "") }
            }

        }
    }

    fun createOthersCategory(selectedDateMillis: Long?, hour: Int, minute: Int) {
        val dateTimeByUI = getCombinedDateTimeMillis(
            selectedDateMillis = selectedDateMillis,
            hour = hour,
            minute = minute
        )
        val dateTime = dateTimeByUI ?: 0L
        viewModelScope.launch(Dispatchers.IO) {
            val myNewTask = _state.value.name
            if (myNewTask.isBlank()) {
                _events.send(Event.ShowMessage("Task name cannot be empty"))
                return@launch
            } else if (userId == null) {
                _events.send(Event.ShowMessage("Error: Cannot create category. User ID is missing"))
            } else if (getCountCategoriesByName(name = "Others", userId = userId) == 0) {
                insertCategory(Category(name = "Others", color = ColorCategoryOthers.toArgb().toLong(), userId = userId))
                val categoryId = getCategoryIdByName(name = "Others", userId = userId).id
                insertTask.invoke(
                    Task(
                        name = myNewTask,
                        dateTime = dateTime,
                        userId = userId,
                        categoryId = categoryId
                    )
                )
                refreshCategories()
                refreshTasks()
                _state.update { it.copy(name = "") }
            } else {
                val categoryId = getCategoryIdByName(name = "Others", userId = userId).id
                insertTask.invoke(
                    Task(
                        name = myNewTask,
                        dateTime = dateTime,
                        userId = userId,
                        categoryId = categoryId
                    )
                )
                refreshTasks()
                refreshCategories()
                _state.update { it.copy(name = "") }
            }
        }
    }

    private fun observeNotificationSettings() {
        viewModelScope.launch {
            userId?.let { userId ->
                observeNotificationSettings(userId).collect { settings ->
                    _areNotificationsEnabled.update { settings?.areNotificationsEnabled ?: false }
                }
            }
        }
    }

    fun toggleNotifications(onPermissionNeeded: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            userId?.let { userId ->
                val currentState = _areNotificationsEnabled.value
                Log.d("TasksViewModel", "🔄 Toggle notifications called. Current state: $currentState")
                
                // Si estamos activando las notificaciones, necesitamos verificar permisos
                if (!currentState) {
                    Log.d("TasksViewModel", "📋 Requesting notification permission...")
                    _events.send(Event.RequestNotificationPermission)
                    return@launch
                }
                
                // Si estamos desactivando, proceder directamente
                Log.d("TasksViewModel", "❌ Disabling notifications...")
                val result = toggleNotifications.invoke(userId, false)
                
                result.onSuccess { settings ->
                    _areNotificationsEnabled.update { settings.areNotificationsEnabled }
                    _events.send(Event.ShowMessage("Notifications disabled for all tasks"))
                    Log.d("TasksViewModel", "✅ Notifications disabled successfully")
                }.onFailure { error ->
                    _events.send(Event.ShowMessage("Error toggling notifications: ${error.message}"))
                    Log.e("TasksViewModel", "❌ Error disabling notifications: ${error.message}")
                }
            } ?: run {
                _events.send(Event.ShowMessage("Error: User ID not found"))
                Log.e("TasksViewModel", "❌ User ID not found")
            }
        }
    }
    
    fun enableNotificationsAfterPermission() {
        viewModelScope.launch(Dispatchers.IO) {
            userId?.let { userId ->
                Log.d("TasksViewModel", "✅ Enabling notifications after permission granted...")
                val result = toggleNotifications.invoke(userId, true)
                
                result.onSuccess { settings ->
                    _areNotificationsEnabled.update { settings.areNotificationsEnabled }
                    _events.send(Event.ShowMessage("Notifications enabled for all tasks"))
                    Log.d("TasksViewModel", "🎉 Notifications enabled successfully!")
                }.onFailure { error ->
                    _events.send(Event.ShowMessage("Error enabling notifications: ${error.message}"))
                    Log.e("TasksViewModel", "❌ Error enabling notifications: ${error.message}")
                }
            }
        }
    }

    sealed class Event {
        data class ShowMessage(val message: String) : Event()
        object RequestNotificationPermission : Event()
    }
}