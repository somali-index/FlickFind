package com.example.flickfind.ui.SearchUI

import com.example.flickfind.DATALAYER.DataClass.DataMovieClass

//data class Movie(
//    val id: String,
//    val title: String,
//    val imageUrl: String = "",
//    val duration: String
//)

// Trạng thái của màn hình tìm kiếm
data class MovieSearchUiState(
    val searchQuery: String = "",             // Chữ người dùng gõ vào ô tìm kiếm
    val movieList: List<DataMovieClass> = emptyList(),    // Danh sách phim tìm thấy
    val isLoading: Boolean = false            // Trạng thái xoay xoay khi đang tìm kiếm
)