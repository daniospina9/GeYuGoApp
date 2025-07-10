package com.example.geyugoapp.domain.users.usecases

import com.example.geyugoapp.domain.users.models.User
import com.example.geyugoapp.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class ObserveAllUsers(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<List<User>> = repository.observeAll()
}