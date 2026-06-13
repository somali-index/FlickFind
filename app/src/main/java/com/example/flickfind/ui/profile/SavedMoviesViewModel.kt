package com.example.flickfind.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.DAO.DAOMovie
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.DATALAYER.Room.AppDatabase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SavedMoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val movieDao: DAOMovie = AppDatabase.getDatabase(application).movieDao()
    private val remote = AppRemote()
    private val repository = Repository(remote, movieDao)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSlowLoading = MutableStateFlow(false)
    val isSlowLoading = _isSlowLoading.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage = _userMessage.asStateFlow()

    private val cloudMovies = MutableStateFlow<List<DataMovie>>(emptyList())
    private var slowLoadingJob: Job? = null

    val savedMovies: StateFlow<List<DataMovie>> = repository.getAllSavedMoviesFlow()
        .combine(cloudMovies) { local, cloud ->
            (local + cloud).distinctBy { it.IDMovie }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        fetchCloudMovies()
    }

    private fun fetchCloudMovies() {
        _isLoading.value = true
        repository.getCurrentDefaultCollectionId { collectionId ->
            if (collectionId != null) {
                repository.getMoviesInCollection(collectionId) { movies ->
                    cloudMovies.value = movies
                    _isLoading.value = false
                }
            } else {
                _isLoading.value = false
            }
        }
    }

    fun removeMovie(movie: DataMovie) {
        startSlowLoadingTimer()
        viewModelScope.launch {
            repository.deleteMovieFromLocal(movie)
            repository.removeMovieFromCurrentAccount(movie.IDMovie) { success ->
                stopSlowLoadingTimer()
                _userMessage.value = if (success) {
                    "Đã xóa '${movie.NameMovie}' khỏi danh sách đã lưu"
                } else {
                    "Đã xóa '${movie.NameMovie}' khỏi máy, nhưng chưa xóa được trên tài khoản"
                }
                fetchCloudMovies()
            }
        }
    }

    fun clearMessage() {
        _userMessage.value = null
    }

    private fun startSlowLoadingTimer() {
        slowLoadingJob?.cancel()
        _isSlowLoading.value = false
        slowLoadingJob = viewModelScope.launch {
            delay(3000)
            _isSlowLoading.value = true
        }
    }

    private fun stopSlowLoadingTimer() {
        slowLoadingJob?.cancel()
        slowLoadingJob = null
        _isSlowLoading.value = false
    }
}
