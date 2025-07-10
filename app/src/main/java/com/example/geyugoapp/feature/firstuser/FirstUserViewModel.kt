package com.example.geyugoapp.feature.firstuser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geyugoapp.domain.users.models.User
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
    val name: String = ""
)

@HiltViewModel
class FirstUserViewModel @Inject constructor(
    private val insertUser: InsertUser
) : ViewModel() {

    private val _state = MutableStateFlow(FirstUserState())
    val state = _state.asStateFlow()

    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    fun setName(name: String) {
        _state.update { it.copy(name = name) }
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            insertUser(user)
        }
    }

    fun insertUser() {
        val userName = _state.value.name
        val notificationTime = 300000L
        val notificationActive = true
        if (userName == "") {
            viewModelScope.launch {
                _events.send(Event.ShowMessage("Name is empty"))
            }
            return
        }
        addUser(
            User(
                name = userName,
                notificationTime = notificationTime,
                notificationsActive = notificationActive
            )
        )
        //getUser
        //_events.send(Event.NavigateToMain)
        //
        viewModelScope.launch {
            _events.send(Event.NavigateToMain)
        }
    }

    sealed class Event {
        data class ShowMessage(val message: String) : Event()
        data class NavigateToMain(val userId: Int): Event()
    }
}