package com.example.flickfind.DATALAYER.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MovieGenre")
data class RoomGenre(
    @PrimaryKey val GenreID: String,
    val GenreName: String,
    val description: String = ""
)
