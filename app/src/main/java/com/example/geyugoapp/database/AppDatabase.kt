package com.example.geyugoapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.geyugoapp.database.dtos.UserDbDto

@Database(entities = [UserDbDto::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}