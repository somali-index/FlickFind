package com.example.flickfind.ui.SearchUI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.DATALAYER.Room.RoomUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieSearchViewModel(application: android.app.Application) : androidx.lifecycle.AndroidViewModel(application) {

    private val repository = Repository(
        remote = AppRemote(),
        movieDao = com.example.flickfind.DATALAYER.Room.AppDatabase.getDatabase(application).movieDao()
    )

    private val _uiState = MutableStateFlow(MovieSearchUiState())
    val uiState = _uiState.asStateFlow()

    private var allMovies: List<DataMovie> = emptyList()
    private var currentUser: RoomUser? = null

    init {
        getMovieList()
        observeSavedMovies()
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            currentUser = repository.getLocalUser()
        }
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

    fun saveMovie(movie: DataMovie) {
        repository.saveMovieToLocal(movie)
        _uiState.update { it.copy(userMessage = "Đã lưu '${movie.NameMovie}' vào máy") }
    }

    fun saveMovieToAccount(movie: DataMovie) {
        val email = currentUser?.Email ?: return
        repository.saveMovieToAccount(email, movie) { success ->
            if (success) {
                _uiState.update { it.copy(userMessage = "Đã đồng bộ '${movie.NameMovie}' vào tài khoản") }
            } else {
                _uiState.update { it.copy(userMessage = "Lỗi khi đồng bộ vào tài khoản") }
            }
        }
    }

    fun saveMovieToCollection(movie: DataMovie, collectionName: String) {
        val email = currentUser?.Email ?: return
        repository.saveMovieToFirestore(email, movie, collectionName) { success ->
            if (success) {
                _uiState.update { it.copy(userMessage = "Đã lưu vào bộ sưu tập $collectionName") }
            } else {
                _uiState.update { it.copy(userMessage = "Lỗi khi lưu vào bộ sưu tập") }
            }
        }
    }

    fun fetchUserCollections() {
        val email = currentUser?.Email ?: return
        repository.fetchUserCollections(email) { collections ->
            _uiState.update { it.copy(collections = collections) }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }
}
