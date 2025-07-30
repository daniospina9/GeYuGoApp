package com.example.geyugoapp.feature.categories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geyugoapp.domain.categories.models.Category
import com.example.geyugoapp.domain.categories.usecases.DeleteCategory
import com.example.geyugoapp.domain.categories.usecases.GetCategoriesByUserId
import com.example.geyugoapp.domain.categories.usecases.InsertCategory
import com.example.geyugoapp.domain.categories.usecases.UpdateCategory
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
    val color: Long = ColorCategoryOthers
)

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val insertCategory: InsertCategory,
    private val getCategoriesByUserId: GetCategoriesByUserId,
    private val updateCategory: UpdateCategory,
    private val deleteCategory: DeleteCategory
) : ViewModel() {

    private val _state = MutableStateFlow(NewCategoryState())
    val state = _state.asStateFlow()

    private val _categoriesByUser = MutableStateFlow<List<Category>>(emptyList())
    val categoriesByUser = _categoriesByUser.asStateFlow()

    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    val currentUserId = savedStateHandle.get<Long?>("userId")

    init {
        refreshCategories()
    }

    private fun refreshCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            if (currentUserId != null) {
                _categoriesByUser.update{  getCategoriesByUserId(userId = currentUserId) }
            }
            return@launch
        }
    }
    fun setNewCategory(newCategory: String) {
        _state.update { it.copy(newCategory = newCategory) }
    }

    fun setColor(color: Long) {
        _state.update { it.copy(color = color) }
    }

    fun insertCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            insertCategory.invoke(category)
            _events.send(Event.ShowMessage("Category Added :)"))
            refreshCategories()
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteCategory.invoke(category)
            _events.send(Event.ShowMessage("Category Deleted!!"))
            refreshCategories()
        }
    }

    fun updateExistCategory(
        category: Category,
        name: String,
        color: Long
    ) {
        val myNewCategory = _state.value.newCategory
        val newColor = _state.value.color
        viewModelScope.launch(Dispatchers.IO) {
            if (myNewCategory.isBlank() && color == newColor) {
                _events.send(Event.ShowMessage("Nothing changed"))
                return@launch
            } else if (myNewCategory.isBlank() && color != newColor) {
                val updateCategoryDbDto = category.copy(name = name, color = newColor)
                updateCategory(updateCategoryDbDto)
                _events.send(Event.ShowMessage("Category Modified"))
                refreshCategories()
                return@launch
            }
            val updateCategoryDbDto = category.copy(name = myNewCategory, color = newColor)
            updateCategory(updateCategoryDbDto)
            _state.update { it.copy(newCategory = "") }
            _events.send(Event.ShowMessage("Category Modified"))
            refreshCategories()
        }
    }

    fun addCategory() {
        val myNewCategory = _state.value.newCategory
        val newColor = _state.value.color
        viewModelScope.launch(Dispatchers.IO) {
            if (myNewCategory.isBlank()) {
                _events.send(Event.ShowMessage("Category is empty"))
                return@launch
            } else if (currentUserId != null) {
                insertCategory(
                    Category(
                        name = myNewCategory,
                        userId = currentUserId,
                        color = newColor
                    )
                )
                _state.update { it.copy(newCategory = "") }
            } else {
                _events.send(Event.ShowMessage("Error: Impossible to save categories without userId"))
                _state.update { it.copy(newCategory = "") }
            }

        }
    }

    sealed class Event {
        data class ShowMessage(val message: String) : Event()
    }
}