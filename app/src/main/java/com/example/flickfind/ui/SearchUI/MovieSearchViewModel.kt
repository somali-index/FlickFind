package com.example.flickfind.ui.SearchUI

import android.graphics.Movie
import androidx.lifecycle.ViewModel
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.DataClass.DataMovieClass
import com.example.flickfind.DATALAYER.Remote.AppRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MovieSearchViewModel : ViewModel() {

    private val repository = Repository(
        remote = AppRemote()
    )

    // Nơi lưu trữ trạng thái bí mật trong ViewModel
    private val _uiState = MutableStateFlow(MovieSearchUiState())
    // Nơi công khai trạng thái ra ngoài cho Giao diện (UI) hứng lấy
    val uiState = _uiState.asStateFlow()

    // Danh sách phim giả lập (Mock Data) để test tính năng tìm kiếm
    private var allMovies: List<DataMovieClass> = emptyList()

    init {
        getMovieList()
    }
    private fun getMovieList() {

        repository.getMovies { movieList ->

            allMovies = movieList

//            _uiState.update {
//                it.copy(
//                    movieList = movieList
//                )
//            }
        }
    }

//    private val dataPhimMau = listOf(
//        Movie("1", "One Piece Film: Red", "", "115 phút"),
//        Movie("2", "Naruto: Trận Chiến Cuối Cùng", "", "112 phút"),
//        Movie("3", "Dragon Ball Super: Broly", "", "100 phút"),
//        Movie("4", "Doraemon: Nobita và Vùng Đất Lý Tưởng", "", "107 phút"),
//        Movie("5", "Conan: Tàu Ngầm Sắt Màu Đen", "", "110 phút")
//    )

    // Hàm này sẽ chạy MỖI KHI NGƯỜI DÙNG GÕ THÊM 1 KÝ TỰ vào ô tìm kiếm
    fun onSearchQueryChange(newQuery: String) {

        _uiState.update {
            it.copy(searchQuery = newQuery)
        }

        if (newQuery.isNotBlank()) {

            val danhSachPhimLocDuoc =
                allMovies.filter { phim ->

                    phim.NameMovie.contains(
                        newQuery,
                        ignoreCase = true
                    )
                }

            _uiState.update {
                it.copy(
                    movieList = danhSachPhimLocDuoc
                )
            }

        } else {

            // Hiện lại toàn bộ phim
            _uiState.update { it.copy(movieList = emptyList()) }

        }
    }
}

