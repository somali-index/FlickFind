package com.example.flickfind.testTinhNang.dataLayerTest.ROOM

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movieData")

data class UNITYTest(

    @PrimaryKey
    val IDMovie: String,

    val NameMovie: String,
    val Description: String,
    val IDStudio: String,
    val URLimage: String
)
