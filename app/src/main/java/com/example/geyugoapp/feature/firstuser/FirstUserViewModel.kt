package com.example.geyugoapp.feature.firstuser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geyugoapp.domain.users.models.User
import com.example.geyugoapp.domain.users.usecases.GetUserById
import com.example.geyugoapp.domain.users.usecases.GetUserIdByOnlineStatus
import com.example.geyugoapp.domain.users.usecases.GetUsersCount
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

data class FirstUserState(
    val isLoading: Boolean = true,
    val name: String = ""
)

@HiltViewModel
class FirstUserViewModel @Inject constructor(
    private val insertUser: InsertUser,
    private val getUserById: GetUserById,
    getUsersCount: GetUsersCount,
    getUserIdByOnlineStatus: GetUserIdByOnlineStatus
) : ViewModel() {

    private val _state = MutableStateFlow(FirstUserState())
    val state = _state.asStateFlow()

    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    fun setName(name: String) {
        _state.update { it.copy(name = name) }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val findOutUsers = getUsersCount()
            if (findOutUsers > 0) {
                val lastUserIdOnline = getUserIdByOnlineStatus(isOnline = true)
                _events.send(Event.NavigateToMain(userId = lastUserIdOnline))
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun saveUser() {
        val userName = _state.value.name
        viewModelScope.launch(Dispatchers.IO) {
            if (userName.isBlank()) {
                _events.send(Event.ShowMessage("Name cannot be empty"))
                return@launch
            }

            val newUser = User(name = userName)

            val newUserId = insertUser(newUser)

            val userFromDb = getUserById(newUserId)

            _events.send(Event.NavigateToMain(userFromDb.id))
        }
    }

    sealed class Event {
        data class ShowMessage(val message: String) : Event()
        data class NavigateToMain(val userId: Long) : Event()
    }
}

