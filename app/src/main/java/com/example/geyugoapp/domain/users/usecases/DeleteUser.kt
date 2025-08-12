package com.example.geyugoapp.domain.users.usecases

import com.example.geyugoapp.domain.users.models.User
import com.example.geyugoapp.repository.UserRepository

class DeleteUser(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User) {
        repository.deleteUser(user)
    }
}