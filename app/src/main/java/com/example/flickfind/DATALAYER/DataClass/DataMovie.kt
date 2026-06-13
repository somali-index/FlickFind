package com.example.flickfind.DATALAYER.DataClass

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class DataMovie(
    val Description: String = "",
    val IDMovie: String = "",
    val IDStudio: String = "",
    val NameMovie: String = "",
    val URLimage: String = "",

    val TimeOneEP: String = "",
    val NummberEP: String = "",

    val Category: String = "",
    val Studio: String = "",
    val Year: String = ""
)

object ListMovieDataSource {
    var movieList by mutableStateOf<List<DataMovie>>(emptyList())
}