package com.example.geyugoapp.domain.users.usecases

import com.example.geyugoapp.domain.users.models.User
import com.example.geyugoapp.repository.UserRepository

class GetAllUsers(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): List<User> {
        return repository.getAllUsers()
    }
}