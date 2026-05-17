package com.example.flickfind.DATALAYER.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movieData")

data class RoomMovies(
    @PrimaryKey
    val IDMovie: String,

    val NameMovie: String,
    val Description: String,
    val IDStudio: String,
    val URLimage: String
)
