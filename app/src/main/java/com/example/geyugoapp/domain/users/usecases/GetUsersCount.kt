package com.example.geyugoapp.domain.users.usecases

import com.example.geyugoapp.repository.UserRepository

class GetUsersCount(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Int {
        return repository.getUsersCount()
    }
}