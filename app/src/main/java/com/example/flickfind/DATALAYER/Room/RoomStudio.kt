package com.example.flickfind.DATALAYER.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "studioData")
data class RoomStudio(
    @PrimaryKey
    val IDStudio: String,
    val StudioName: String
)
