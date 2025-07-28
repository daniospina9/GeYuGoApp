package com.example.geyugoapp.feature.tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geyugoapp.domain.categories.models.Category
import com.example.geyugoapp.domain.categories.usecases.GetCategoriesByUserId
import com.example.geyugoapp.domain.task.usecases.InsertTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TasksState(
    val name: String = ""
)

@HiltViewModel
class TasksViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCategoriesByUserId: GetCategoriesByUserId,
    private val insertTask: InsertTask
): ViewModel() {

    private val _categoriesByUser = MutableStateFlow<List<Category>>(emptyList())
    val categoriesByUser = _categoriesByUser.asStateFlow()

    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(TasksState())
    val state = _state.asStateFlow()

    val userId = savedStateHandle.get<Long?>("userId")

    init {
        refreshCategories()
    }

    private fun refreshCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            if (userId != null) {
                _categoriesByUser.update { getCategoriesByUserId(userId) }
            } else {
                _events.send(Event.ShowMessage("Categories from userId not fount"))
            }
        }
    }

    fun setName(name: String) {
        _state.update { it.copy(name = name) }
    }

    sealed class Event{
        data class ShowMessage(val message: String): Event()
    }
}