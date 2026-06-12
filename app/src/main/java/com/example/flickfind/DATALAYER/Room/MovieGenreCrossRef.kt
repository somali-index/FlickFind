package com.example.flickfind.DATALAYER.Room

import androidx.room.Entity

@Entity(
    tableName = "Genre_Movie",
    primaryKeys = ["GenreID", "IDMovie"]
)
data class MovieGenreCrossRef(
    val GenreID: String,
    val IDMovie: String
)
