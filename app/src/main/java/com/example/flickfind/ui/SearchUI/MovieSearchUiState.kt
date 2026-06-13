package com.example.flickfind.ui.SearchUI

import com.example.flickfind.DATALAYER.DataClass.DataCollection
import com.example.flickfind.DATALAYER.DataClass.DataMovie

//data class Movie(
//    val id: String,
//    val title: String,
//    val imageUrl: String = "",
//    val duration: String
//)

// Trạng thái của màn hình tìm kiếm
data class MovieSearchUiState(
    val searchQuery: String = "",             // Chữ người dùng gõ vào ô tìm kiếm
    val movieList: List<DataMovie> = emptyList(),    // Danh sách phim tìm thấy
    val savedMovieIds: Set<String> = emptySet(),     // Danh sách ID phim đã lưu
    val collections: List<DataCollection> = emptyList(), // Danh sách bộ sưu tập (Cloud)
    val userMessage: String? = null,           // Thông báo người dùng
    val isLoading: Boolean = false            // Trạng thái xoay xoay khi đang tìm kiếm
)