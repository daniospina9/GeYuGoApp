package com.example.geyugoapp.feature.tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geyugoapp.domain.categories.models.Category
import com.example.geyugoapp.domain.categories.usecases.GetCategoriesByUserId
import com.example.geyugoapp.domain.categories.usecases.GetCategoryIdByName
import com.example.geyugoapp.domain.categories.usecases.GetCountCategoriesByName
import com.example.geyugoapp.domain.categories.usecases.InsertCategory
import com.example.geyugoapp.domain.task.models.Task
import com.example.geyugoapp.domain.task.usecases.DeleteTask
import com.example.geyugoapp.domain.task.usecases.GetCountTasksByCategory
import com.example.geyugoapp.domain.task.usecases.GetTasksByUserId
import com.example.geyugoapp.domain.task.usecases.InsertTask
import com.example.geyugoapp.domain.task.usecases.UpdateTask
import com.example.geyugoapp.feature.categories.CategoryWithTaskCount
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

data class TasksState(
    val name: String = ""
)

data class CategoryWithTaskCount(
    val category: Category,
    val taskCount: Int
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
    private val getCountTasksByCategory: GetCountTasksByCategory
) : ViewModel() {

    private val _categoriesByUser = MutableStateFlow<List<Category>>(emptyList())
    val categoriesByUser = _categoriesByUser.asStateFlow()

    private val _categoriesWithCounts = MutableStateFlow<List<CategoryWithTaskCount>>(emptyList())
    val categoriesWithCounts = _categoriesWithCounts.asStateFlow()

    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(TasksState())
    val state = _state.asStateFlow()

    private val _tasksByUserId = MutableStateFlow<List<Task>>(emptyList())
    val tasksByUserId = _tasksByUserId.asStateFlow()

    val userId = savedStateHandle.get<Long?>("userId")

    init {
        refreshCategories()
        refreshTasks()
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

    sealed class Event {
        data class ShowMessage(val message: String) : Event()
    }
}