package com.example.geyugoapp.feature.categories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geyugoapp.domain.categories.models.Category
import com.example.geyugoapp.domain.categories.usecases.InsertCategory
import com.example.geyugoapp.domain.categories.usecases.ObserveAllCategories
import com.example.geyugoapp.ui.theme.ColorCategoryOthers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NewCategoryState(
    val newCategory: String = "",
    val color: Long = ColorCategoryOthers
)

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeAllCategories: ObserveAllCategories,
    private val insertCategory: InsertCategory
) : ViewModel() {

    private val _state = MutableStateFlow(NewCategoryState())
    val state = _state.asStateFlow()

    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    val currentUserId = savedStateHandle.get<Long?>("userId")

    val categories = observeAllCategories()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categoriesByUser = categories.map { flowList ->
        flowList.filter { it.userId == currentUserId }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setNewCategory(newCategory: String) {
        _state.update { it.copy(newCategory = newCategory) }
    }

    fun setColor(color: Long) {
        _state.update { it.copy(color = color) }
    }

    fun insertCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            insertCategory.invoke(category)
        }
    }

    fun addCategory() {
        val myNewCategory = _state.value.newCategory
        val newColor = _state.value.color
        viewModelScope.launch(Dispatchers.IO) {
            if (myNewCategory.isNullOrBlank()) {
                _events.send(Event.ShowMessage("Category is empty"))
                return@launch
            }
            insertCategory(
                Category(
                    name = myNewCategory,
                    userId = currentUserId,
                    color = newColor
                )
            )
            _state.update { it.copy(newCategory = "") }
        }
    }

    sealed class Event {
        data class ShowMessage(val message: String) : Event()
    }
}