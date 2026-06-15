package com.flickfind.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flickfind.app.data.model.Movie
import com.flickfind.app.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val trendingMovies: List<Movie> = emptyList(),
    val upcomingMovies: List<Movie> = emptyList(),
    val topRatedMovies: List<Movie> = emptyList(),
    val popularMovies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel : ViewModel() {
    private val repository = MovieRepository()
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val trendingResult = repository.getTrending()
            val upcomingResult = repository.getUpcoming()
            val topRatedResult = repository.getTopRated()
            val popularResult = repository.getPopular()
            
            if (trendingResult.isSuccess && popularResult.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    trendingMovies = trendingResult.getOrNull() ?: emptyList(),
                    upcomingMovies = upcomingResult.getOrNull() ?: emptyList(),
                    topRatedMovies = topRatedResult.getOrNull() ?: emptyList(),
                    popularMovies = popularResult.getOrNull() ?: emptyList()
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Lỗi khi tải dữ liệu. Vui lòng kiểm tra kết nối mạng."
                )
            }
        }
    }
}
