package com.example.flickfind.DATALAYER.Room

import androidx.room.Entity

// Nối Phim với Thể loại (Nhiều - Nhiều)
@Entity(tableName = "Genre_Movie", primaryKeys = ["IDMovie", "GenreID"])
data class MovieGenreCrossRef(
    val IDMovie: String,
    val GenreID: String
)

// Nối Phim với Studio (Nhiều - Nhiều)
@Entity(tableName = "Studio_Movie", primaryKeys = ["IDMovie", "IDStudio"])
data class MovieStudioCrossRef(
    val IDMovie: String,
    val IDStudio: String
)
