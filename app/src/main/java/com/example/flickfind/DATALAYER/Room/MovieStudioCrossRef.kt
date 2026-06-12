package com.example.flickfind.DATALAYER.Room

import androidx.room.Entity

@Entity(
    tableName = "Studio_Movie",
    primaryKeys = ["IDStudio", "IDMovie"]
)
data class MovieStudioCrossRef(
    val IDStudio: String,
    val IDMovie: String
)
