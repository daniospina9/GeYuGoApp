package com.example.geyugoapp.datasource

import com.example.geyugoapp.database.UserDao
import com.example.geyugoapp.datasource.converters.toDbDto
import com.example.geyugoapp.datasource.converters.toUser
import com.example.geyugoapp.domain.users.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDataSourceImpl(
    private val userDao: UserDao
): UserDataSource {

    override fun observeAll(): Flow<List<User>> = userDao.observeAll().map { flowList ->
        flowList.map { it.toUser() }
    }

    override suspend fun insert(user: User): Long {
        return userDao.insert(user.toDbDto())
    }

    override suspend fun getUserById(userId: Long): User {
        return userDao.getUserById(userId).toUser()
    }

    override suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers().map { it.toUser() }
    }

    override suspend fun deleteUser(user: User) {
        userDao.deleteUser(user.toDbDto())
    }

    override suspend fun getUsersCount(): Int {
        return userDao.getUsersCount()
    }

    override suspend fun updateAllUsersOnlineStatus(online: Boolean) {
        userDao.updateAllUsersOnlineStatus(online)
    }

    override suspend fun updateUserOnlineStatus(userId: Long, online: Boolean) {
        userDao.updateUserOnlineStatus(userId, online)
    }

    override suspend fun getUserIdByOnlineStatus(isOnline: Boolean): Long {
        return userDao.getUserIdByOnlineStatus(isOnline)
    }
}