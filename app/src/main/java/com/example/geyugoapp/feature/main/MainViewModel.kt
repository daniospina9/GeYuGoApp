package com.example.geyugoapp.feature.main

import android.icu.util.Calendar
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geyugoapp.domain.categories.models.Category
import com.example.geyugoapp.domain.categories.usecases.GetCategoriesByUserId
import com.example.geyugoapp.domain.categories.usecases.GetCategoryIdByName
import com.example.geyugoapp.domain.categories.usecases.GetCountCategoriesByName
import com.example.geyugoapp.domain.categories.usecases.InsertCategory
import com.example.geyugoapp.domain.task.models.Task
import com.example.geyugoapp.domain.task.usecases.InsertTask
import com.example.geyugoapp.domain.users.models.User
import com.example.geyugoapp.domain.users.usecases.GetAllUsers
import com.example.geyugoapp.domain.users.usecases.GetUserById
import com.example.geyugoapp.domain.users.usecases.UpdateAllUsersOnlineStatus
import com.example.geyugoapp.domain.users.usecases.UpdateUserOnlineStatus
import com.example.geyugoapp.feature.tasks.DrawerTaskState
import com.example.geyugoapp.feature.tasks.TaskDate
import com.example.geyugoapp.feature.tasks.TasksState
import com.example.geyugoapp.ui.theme.ColorCategoryOthers
import com.example.geyugoapp.ui.util.tasks.getCombinedDateTimeMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ModalDrawerState(
    val expanded: Boolean = false
)

data class TasksState(
    val name: String = ""
)

data class DrawerTaskState(
    val expandedTaskMenu: Boolean = false,
    val categorySelection: String = "Category",
    val showDateTaskMenuDialog: Boolean = false,
    val showTimePicker: Boolean = false,
    val timeTaskMessage: String = "Add Time"
)

@HiltViewModel
class MainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserById: GetUserById,
    private val getAllUsers: GetAllUsers,
    updateAllUsersOnlineStatus: UpdateAllUsersOnlineStatus,
    updateUserOnlineStatus: UpdateUserOnlineStatus,
    private val getCategoriesByUserId: GetCategoriesByUserId,
    private val getCountCategoriesByName: GetCountCategoriesByName,
    private val insertCategory: InsertCategory,
    private val getCategoryIdByName: GetCategoryIdByName,
    private val insertTask: InsertTask
) : ViewModel() {

    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    private val _userName = MutableStateFlow<String?>("")
    val userName = _userName.asStateFlow()

    private val _expanded = MutableStateFlow(ModalDrawerState())
    val expanded = _expanded.asStateFlow()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()

    private val _state = MutableStateFlow(TasksState())
    val state = _state.asStateFlow()

    private val _drawerTaskState = MutableStateFlow(DrawerTaskState())
    val drawerTaskState = _drawerTaskState.asStateFlow()

    private val _datesState = MutableStateFlow(TaskDate())
    val datesState = _datesState.asStateFlow()

    private val _categoriesByUser = MutableStateFlow<List<Category>>(emptyList())
    val categoriesByUser = _categoriesByUser.asStateFlow()

    val userId = savedStateHandle.get<Long>("userId")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (userId != null) {
                _events.send(Event.ShowMessage("Welcome, ${getUserById(userId).name}!"))
                updateAllUsersOnlineStatus(online = false)
                updateUserOnlineStatus(userId = userId, online = true)
                refreshUser(userId)
                _users.update { getAllUsers() }
            } else {
                _events.send(Event.ShowMessage("Error: User ID not found"))
            }
        }
        refreshCategories()
        val calendar = Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        _datesState.update {
            it.copy(
                day = calendar.get(Calendar.DAY_OF_MONTH),
                month = calendar.get(Calendar.MONTH) + 1,
                year = calendar.get(Calendar.YEAR),
                calendar = calendar
            )
        }
    }

    private fun refreshCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            if (userId != null) {
                _categoriesByUser.update { getCategoriesByUserId(userId = userId) }
            } else {
                _events.send(Event.ShowMessage("Error: Could not load categories. User ID is missing or invalid"))
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
                insertCategory(Category(name = "Others", color = ColorCategoryOthers, userId = userId))
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
                refreshCategories()
                _state.update { it.copy(name = "") }
            }
        }
    }

    fun setName(name: String) {
        _state.update { it.copy(name = name) }
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

    fun setExpanded(expanded: Boolean) {
        _expanded.update { it.copy(expanded = expanded) }
    }

    fun refreshUser(userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _userName.update { getUserById(userId).name }
        }
    }

    sealed class Event {
        data class ShowMessage(val message: String) : Event()
    }

}