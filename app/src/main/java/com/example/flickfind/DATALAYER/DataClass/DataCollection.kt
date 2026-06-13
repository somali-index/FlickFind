package com.example.flickfind.DATALAYER.DataClass

data class DataCollection(
    val IDCollection: String = "",
    val IDUser: String = "",
    val CollectionName: String = "",
    val CreateAt: Long = System.currentTimeMillis()
)
