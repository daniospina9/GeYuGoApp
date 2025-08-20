package com.example.geyugoapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.geyugoapp.database.dtos.CategoryDbDto
import com.example.geyugoapp.database.dtos.NotificationSettingsDbDto
import com.example.geyugoapp.database.dtos.TaskDbDto
import com.example.geyugoapp.database.dtos.UserDbDto

@Database(entities = [
    UserDbDto::class,
    CategoryDbDto::class,
    TaskDbDto::class,
    NotificationSettingsDbDto::class
                     ], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun taskDao(): TaskDao
    abstract fun notificationSettingsDao(): NotificationSettingsDao
}