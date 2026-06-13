package com.example.flickfind.DATALAYER.DataClass

data class DataCollection(
    val IDCollection: String = "",
    val CollectionName: String = "",
    val IDUser: String = ""
)

data class CollectionMovie(
    val IDMovie: String = "",
    val IDCollection: String = ""
)
