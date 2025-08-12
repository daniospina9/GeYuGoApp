package com.example.geyugoapp.feature.settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geyugoapp.domain.users.models.User
import com.example.geyugoapp.domain.users.usecases.DeleteUser
import com.example.geyugoapp.domain.users.usecases.GetAllUsers
import com.example.geyugoapp.domain.users.usecases.GetUserById
import com.example.geyugoapp.domain.users.usecases.InsertUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DrawersState(
    val expanded: Boolean = false,
    val showDialog: Boolean = false,
    val newUserDialog: Boolean = false,
    val newUser: String = ""
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAllUsers: GetAllUsers,
    private val getUserById: GetUserById,
    private val deleteUser: DeleteUser,
    private val insertUser: InsertUser
) : ViewModel() {

    private val _drawersState = MutableStateFlow(DrawersState())
    val drawersState = _drawersState.asStateFlow()

    private val _userName = MutableStateFlow<String?>("")
    val userName = _userName.asStateFlow()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()
    private val _events = Channel<Event>()
    val event = _events.receiveAsFlow()

    val userId = savedStateHandle.get<Long?>("userId")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (userId != null) {
                refreshUser(userId)
                _users.update { getAllUsers() }
            } else {
                _events.send(Event.ShowMessage("Error: User ID not found"))
            }
        }
    }

    fun refreshUser(userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _userName.update { getUserById(userId).name }
        }
    }

    fun setExpanded(expanded: Boolean) {
        _drawersState.update { it.copy(expanded = expanded) }
    }

    fun setShowDialog(showDialog: Boolean) {
        _drawersState.update { it.copy(showDialog = showDialog) }
    }

    fun setNewUserDialog(newUserDialog: Boolean) {
        _drawersState.update { it.copy(newUserDialog = newUserDialog) }
    }

    fun setNewUser(newUser: String) {
        _drawersState.update { it.copy(newUser = newUser) }
    }

    fun insertNewUser() {
        val newUser = _drawersState.value.newUser
        viewModelScope.launch(Dispatchers.IO) {
            if (newUser.isBlank()) {
                _events.send(Event.ShowMessage("UserName cannot be empty"))
                _drawersState.update { it.copy(newUserDialog = false) }
                return@launch
            }

            val newUser = User(name = newUser)

            val newUserId = insertUser(newUser)

            val userFromDb = getUserById(newUserId)

            _events.send(Event.NavigateToMain(userFromDb.id))
        }
    }

    fun eliminateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteUser(user)
            val usersList = getAllUsers()
            if (usersList.isNotEmpty()) {
                val lastUser = usersList.maxBy { it.id }.id
                _events.send(Event.NavigateToMain(userId = lastUser))
            } else {
                _events.send(Event.NavigateToFirstUser)
            }
        }
    }

    sealed class Event {
        data class ShowMessage(val message: String): Event()
        data class NavigateToMain(val userId: Long): Event()
        data object NavigateToFirstUser: Event()
    }
}