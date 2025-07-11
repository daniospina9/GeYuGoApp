package com.example.geyugoapp.domain.users.usecases

import com.example.geyugoapp.domain.users.models.User
import com.example.geyugoapp.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserById(
    private val repository: UserRepository
) {
    operator fun invoke(userId: Int): User? = User(
        id = 1,
        name = "Agapito",
        notificationTime = 0L,
        notificationsActive = true
    )
}