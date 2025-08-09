package com.example.geyugoapp.feature.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geyugoapp.domain.users.models.User
import com.example.geyugoapp.domain.users.usecases.GetAllUsers
import com.example.geyugoapp.domain.users.usecases.GetUserById
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

@HiltViewModel
class MainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserById: GetUserById,
    private val getAllUsers: GetAllUsers
) : ViewModel() {

    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    private val _userName = MutableStateFlow<String?>("")
    val userName = _userName.asStateFlow()

    private val _expanded = MutableStateFlow(ModalDrawerState())
    val expanded = _expanded.asStateFlow()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()

    val userId = savedStateHandle.get<Long>("userId")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (userId != null) {
                _events.send(Event.ShowMessage("Welcome, ${getUserById(userId).name}!"))
                refreshUser(userId)
                _users.update { getAllUsers() }
            } else {
                _events.send(Event.ShowMessage("Error: User ID not found"))
            }
        }
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