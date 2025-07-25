package com.example.geyugoapp.domain.users.usecases

import com.example.geyugoapp.domain.users.models.User
import com.example.geyugoapp.repository.UserRepository

class GetUserById(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: Long): User {
        return repository.getUserById(userId)
    }
}