package com.example.flickfind.ui.home

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.DataClass.DataCollection
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val movieList: List<DataMovie> = emptyList(),
    val savedMovieIds: Set<String> = emptySet(),
    val collections: List<DataCollection> = emptyList(),
    val isLoading: Boolean = true,
    val isSlowLoading: Boolean = false,
    val userMessage: String? = null
)

class HomeViewModel(application: android.app.Application) : AndroidViewModel(application) {

    private val repository = Repository(
        remote = AppRemote(),
        movieDao = com.example.flickfind.DATALAYER.Room.AppDatabase.getDatabase(application).movieDao()
    )

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()

    private val forbiddenNames = listOf("Danh sách đã lưu", "LƯU NHANH", "Quick Save", "Saved Movies", "Default")
    private var slowLoadingJob: Job? = null

    init {
        getMovieListHome()
        observeSavedMovies()
    }

    private fun observeSavedMovies() {
        viewModelScope.launch {
            repository.getSavedMovieIdsFlow().collect { ids ->
                _homeUiState.update { it.copy(savedMovieIds = ids) }
            }
        }
    }

    fun saveMovie(movie: DataMovie) {
        repository.saveMovieToLocal(movie)
        _homeUiState.update { it.copy(userMessage = "Đã lưu nhanh '${movie.NameMovie}' vào máy") }
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
            _homeUiState.update { it.copy(userMessage = message) }
        }
    }

    fun saveMovieToCollection(movie: DataMovie, collectionName: String) {
        startSlowLoadingTimer()
        repository.saveCurrentUserMovieToCollection(movie, collectionName) { success ->
            stopSlowLoadingTimer()
            val message = if (success) {
                "Đã thêm '${movie.NameMovie}' vào bộ sưu tập '$collectionName'"
            } else {
                "Không tìm thấy bộ sưu tập '$collectionName'"
            }
            _homeUiState.update { it.copy(userMessage = message) }
        }
    }

    fun fetchUserCollections() {
        startSlowLoadingTimer()
        repository.fetchCurrentUserCollections { list ->
            stopSlowLoadingTimer()
            val filteredList = list.filter { col ->
                !forbiddenNames.any { it.equals(col.CollectionName.trim(), ignoreCase = true) }
            }
            _homeUiState.update { it.copy(collections = filteredList) }
        }
    }

    fun clearMessage() {
        _homeUiState.update { it.copy(userMessage = null) }
    }

    fun logout() {
        viewModelScope.launch {
            repository.clearLocalUser()
            FirebaseAuth.getInstance().signOut()
        }
    }

    private fun getMovieListHome() {
        repository.getMovies { movieList ->
            _homeUiState.update {
                it.copy(movieList = movieList, isLoading = false)
            }
        }
    }

    private fun startSlowLoadingTimer() {
        slowLoadingJob?.cancel()
        _homeUiState.update { it.copy(isSlowLoading = false) }
        slowLoadingJob = viewModelScope.launch {
            delay(3000)
            _homeUiState.update { it.copy(isSlowLoading = true) }
        }
    }

    private fun stopSlowLoadingTimer() {
        slowLoadingJob?.cancel()
        slowLoadingJob = null
        _homeUiState.update { it.copy(isSlowLoading = false) }
    }
}
