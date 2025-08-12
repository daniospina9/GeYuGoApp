package com.example.geyugoapp.domain.users.usecases

import com.example.geyugoapp.repository.UserRepository

class UpdateAllUsersOnlineStatus(
    private val repository: UserRepository
) {
    suspend operator fun invoke(online: Boolean) {
        repository.updateAllUsersOnlineStatus(online)
    }
}