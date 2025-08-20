package com.example.geyugoapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.geyugoapp.database.dtos.NotificationSettingsDbDto
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationSettingsDao {

    @Query("SELECT * FROM notification_settings WHERE userId = :userId LIMIT 1")
    suspend fun getNotificationSettingsByUserId(userId: Long): NotificationSettingsDbDto?

    @Query("SELECT * FROM notification_settings WHERE userId = :userId LIMIT 1")
    fun observeNotificationSettingsByUserId(userId: Long): Flow<NotificationSettingsDbDto?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotificationSettings(notificationSettings: NotificationSettingsDbDto): Long

    @Update
    suspend fun updateNotificationSettings(notificationSettings: NotificationSettingsDbDto)

    @Query("DELETE FROM notification_settings WHERE userId = :userId")
    suspend fun deleteNotificationSettingsByUserId(userId: Long)
}