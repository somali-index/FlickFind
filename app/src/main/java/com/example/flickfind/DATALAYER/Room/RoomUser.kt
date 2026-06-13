package com.example.flickfind.DATALAYER.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userData")
data class RoomUser(
    @PrimaryKey
    val IDUser: String,
    val Email: String,
    val Pass: String,
    val UserName: String,
    val avatar: String = ""
)
