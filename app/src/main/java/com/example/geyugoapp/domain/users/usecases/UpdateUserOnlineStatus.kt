package com.example.geyugoapp.domain.users.usecases

import com.example.geyugoapp.repository.UserRepository

class UpdateUserOnlineStatus(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: Long, online: Boolean) {
        repository.updateUserOnlineStatus(userId, online)
    }
}