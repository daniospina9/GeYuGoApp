package com.example.geyugoapp.feature.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

@HiltViewModel
class MainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserById: GetUserById
) : ViewModel() {

    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    private val _userName = MutableStateFlow<String?>("")
    val userName = _userName.asStateFlow()

    val userId = savedStateHandle.get<Long?>("userId")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _events.send(Event.ShowMessage("Welcome ${getUserById(userId)?.name.orEmpty()}"))
        }
        refreshUser()
    }

    fun refreshUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _userName.update { getUserById(userId)?.name }
        }
    }

    sealed class Event {
        data class ShowMessage(val message: String) : Event()
    }

}