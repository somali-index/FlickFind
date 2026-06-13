package com.example.flickfind.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.DATALAYER.Room.AppDatabase
import com.example.flickfind.DATALAYER.Room.RoomUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository(
        remote = AppRemote(),
        movieDao = AppDatabase.getDatabase(application).movieDao()
    )

    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState = _uiState.asStateFlow()

    private var currentUser: RoomUser? = null

    init {
        loadCurrentUser()
        observeSavedMovies()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            currentUser = repository.getLocalUser()
        }
    }

    private fun observeSavedMovies() {
        viewModelScope.launch {
            repository.getSavedMovieIdsFlow().collect { ids ->
                _uiState.update { state ->
                    state.copy(
                        savedMovieIds = ids,
                        isSaved = state.movie?.let { ids.contains(it.IDMovie) } ?: false
                    )
                }
            }
        }
    }

    fun getMovieById(movieId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getMovies { movieList ->
                val movie = movieList.find { it.IDMovie == movieId }
                _uiState.update { state ->
                    state.copy(
                        movie = movie,
                        isLoading = false,
                        isSaved = movie?.let { state.savedMovieIds.contains(it.IDMovie) } ?: false
                    )
                }
            }
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
