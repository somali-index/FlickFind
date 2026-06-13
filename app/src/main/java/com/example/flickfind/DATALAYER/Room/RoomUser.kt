package com.example.flickfind.DATALAYER.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class RoomUser(
    @PrimaryKey val Email: String,
    val UserName: String,
    val avatar: String = ""
)
