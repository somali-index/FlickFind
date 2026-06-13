package com.example.flickfind.ui.SearchUI

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.DATALAYER.Room.AppDatabase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieSearchViewModel(application: android.app.Application) : AndroidViewModel(application) {

    private val repository = Repository(
        remote = AppRemote(),
        movieDao = AppDatabase.getDatabase(application).movieDao()
    )

    private val _uiState = MutableStateFlow(MovieSearchUiState())
    val uiState = _uiState.asStateFlow()

    private var allMovies: List<DataMovie> = emptyList()
    private var slowLoadingJob: Job? = null

    init {
        getMovieList()
        observeSavedMovies()
    }

    private fun observeSavedMovies() {
        viewModelScope.launch {
            repository.getSavedMovieIdsFlow().collect { ids ->
                _uiState.update { it.copy(savedMovieIds = ids) }
            }
        }
    }

    private fun getMovieList() {
        repository.getMovies { movieList ->
            allMovies = movieList
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _uiState.update { it.copy(searchQuery = newQuery) }

        if (newQuery.isNotBlank()) {
            val filteredMovies = allMovies.filter { movie ->
                movie.NameMovie.contains(newQuery, ignoreCase = true)
            }
            _uiState.update { it.copy(movieList = filteredMovies) }
        } else {
            _uiState.update { it.copy(movieList = emptyList()) }
        }
    }

    fun saveMovie(movie: DataMovie) {
        repository.saveMovieToLocal(movie)
        _uiState.update { it.copy(userMessage = "Đã lưu '${movie.NameMovie}' vào máy") }
    }

    fun saveMovieToAccount(movie: DataMovie) {
        startSlowLoadingTimer()
        repository.saveCurrentUserMovieToAccount(movie) { success ->
            stopSlowLoadingTimer()
            val message = if (success) {
                "Đã đồng bộ '${movie.NameMovie}' vào tài khoản"
            } else {
                "Lỗi khi đồng bộ vào tài khoản"
            }
            _uiState.update { it.copy(userMessage = message) }
        }
    }

    fun saveMovieToCollection(movie: DataMovie, collectionName: String) {
        startSlowLoadingTimer()
        repository.saveCurrentUserMovieToCollection(movie, collectionName) { success ->
            stopSlowLoadingTimer()
            val message = if (success) {
                "Đã lưu vào bộ sưu tập $collectionName"
            } else {
                "Lỗi khi lưu vào bộ sưu tập"
            }
            _uiState.update { it.copy(userMessage = message) }
        }
    }

    fun fetchUserCollections() {
        startSlowLoadingTimer()
        repository.fetchCurrentUserCollections { collections ->
            stopSlowLoadingTimer()
            _uiState.update { it.copy(collections = collections) }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }

    private fun startSlowLoadingTimer() {
        slowLoadingJob?.cancel()
        _uiState.update { it.copy(isSlowLoading = false) }
        slowLoadingJob = viewModelScope.launch {
            delay(3000)
            _uiState.update { it.copy(isSlowLoading = true) }
        }
    }

    private fun stopSlowLoadingTimer() {
        slowLoadingJob?.cancel()
        slowLoadingJob = null
        _uiState.update { it.copy(isSlowLoading = false) }
    }
}
