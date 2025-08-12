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

    override suspend fun getAllUsers(): List<User> {
        return userDataSource.getAllUsers()
    }

    override suspend fun deleteUser(user: User) {
        userDataSource.deleteUser(user)
    }

    override suspend fun getUsersCount(): Int {
        return userDataSource.getUsersCount()
    }

    override suspend fun updateAllUsersOnlineStatus(online: Boolean) {
        userDataSource.updateAllUsersOnlineStatus(online)
    }

    override suspend fun updateUserOnlineStatus(userId: Long, online: Boolean) {
        userDataSource.updateUserOnlineStatus(userId, online)
    }

    override suspend fun getUserIdByOnlineStatus(isOnline: Boolean): Long {
        return userDataSource.getUserIdByOnlineStatus(isOnline)
    }
}