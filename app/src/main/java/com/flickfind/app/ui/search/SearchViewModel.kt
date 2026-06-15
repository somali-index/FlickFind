package com.flickfind.app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flickfind.app.data.model.Genre
import com.flickfind.app.data.model.Movie
import com.flickfind.app.data.repository.MovieRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val genres: List<Genre> = emptyList(),
    val selectedGenre: Genre? = null,
    val selectedYear: Int? = null,
    val results: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@OptIn(FlowPreview::class)
class SearchViewModel : ViewModel() {
    private val repository = MovieRepository()
    
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val searchQueryFlow = MutableStateFlow("")

    init {
        viewModelScope.launch {
            val genreResult = repository.getGenres()
            if (genreResult.isSuccess) {
                _uiState.value = _uiState.value.copy(genres = genreResult.getOrNull() ?: emptyList())
            }
        }
        viewModelScope.launch {
            searchQueryFlow
                .debounce(500L)
                .collect { 
                    performSearch()
                }
        }
    }

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
        searchQueryFlow.value = query
    }

    fun onGenreSelected(genre: Genre?) {
        _uiState.value = _uiState.value.copy(selectedGenre = genre)
        performSearch()
    }

    fun onYearSelected(year: Int?) {
        _uiState.value = _uiState.value.copy(selectedYear = year)
        performSearch()
    }

    private fun performSearch() {
        val state = _uiState.value
        viewModelScope.launch {
            if (state.query.isBlank() && state.selectedGenre == null && state.selectedYear == null) {
                _uiState.value = _uiState.value.copy(results = emptyList(), error = null)
                return@launch
            }
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val result = if (state.query.isNotBlank()) {
                repository.searchMovies(state.query)
            } else {
                repository.discoverMovies(state.selectedGenre?.id, state.selectedYear)
            }

            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    results = result.getOrNull() ?: emptyList()
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Không thể tìm kiếm"
                )
            }
        }
    }
}
