package com.example.flickfind.ui.SearchUI

import androidx.lifecycle.ViewModel
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.DATALAYER.Remote.AppRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MovieSearchViewModel : ViewModel() {

    private val repository = Repository(
        remote = AppRemote()
    )

    private val _uiState = MutableStateFlow(MovieSearchUiState())
    val uiState = _uiState.asStateFlow()

    private var allMovies: List<DataMovie> = emptyList()

    init {
        getMovieList()
    }

    private fun getMovieList() {
        repository.getMovies { movieList ->
            allMovies = movieList
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _uiState.update {
            it.copy(searchQuery = newQuery)
        }

        if (newQuery.isNotBlank()) {
            val danhSachPhimLocDuoc = allMovies.filter { phim ->
                phim.NameMovie.contains(newQuery, ignoreCase = true)
            }
            _uiState.update {
                it.copy(movieList = danhSachPhimLocDuoc)
            }
        } else {
            _uiState.update { it.copy(movieList = emptyList()) }
        }
    }
}
