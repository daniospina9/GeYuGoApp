package com.example.geyugoapp.feature.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geyugoapp.domain.users.usecases.GetUserById
import com.example.geyugoapp.domain.users.usecases.ObserveAllUsers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getUserById: GetUserById
) : ViewModel() {

    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    val user = savedStateHandle.get<Int>("userId")?.let { userId -> getUserById(userId) }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _events.send(Event.ShowMessage("Welcome ${user?.name.orEmpty()}"))
        }
    }

    sealed class Event {
        data class ShowMessage(val message: String) : Event()
    }

}