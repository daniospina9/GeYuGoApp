package com.example.geyugoapp.domain.users.usecases

import com.example.geyugoapp.repository.UserRepository

class GetUserIdByOnlineStatus(
    private val repository: UserRepository
) {
    suspend operator fun invoke(isOnline: Boolean): Long {
        return repository.getUserIdByOnlineStatus(isOnline)
    }
}