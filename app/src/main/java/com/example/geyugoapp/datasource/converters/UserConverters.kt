package com.example.geyugoapp.datasource.converters

import com.example.geyugoapp.database.dtos.UserDbDto
import com.example.geyugoapp.domain.users.models.User

fun User.toDbDto(): UserDbDto {
    return UserDbDto (
        id = id,
        name = name,
        notificationTime = notificationTime,
        notificationsActive = notificationsActive,
        online = online
    )
}

fun UserDbDto.toUser(): User {
    return User (
        id = id,
        name = name,
        notificationTime = notificationTime,
        notificationsActive = notificationsActive,
        online = online
    )
}