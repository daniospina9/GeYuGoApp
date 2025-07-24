package com.example.geyugoapp.repository

import com.example.geyugoapp.datasource.UserDataSource
import com.example.geyugoapp.domain.users.models.User
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
): UserRepository {

    override fun observeAll(): Flow<List<User>> = userDataSource.observeAll()

    override suspend fun insert(user: User): Long {
        return userDataSource.insert(user)
    }

    override suspend fun getUserById(userId: Long): User {
        return userDataSource.getUserById(userId)
    }
}