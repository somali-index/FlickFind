package com.example.flickfind.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.DAO.DAOMovie
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.DATALAYER.Room.AppDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class SavedMoviesViewModel(application: Application) : AndroidViewModel(application) {
    
    private val movieDao: DAOMovie = AppDatabase.getDatabase(application).movieDao()
    private val remote = AppRemote()
    private val repository = Repository(remote, movieDao)
    private val auth = FirebaseAuth.getInstance()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val cloudMovies = MutableStateFlow<List<DataMovie>>(emptyList())

    // Kết hợp cả phim Local và phim Cloud từ bộ sưu tập "Default"
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
        val userEmail = auth.currentUser?.email ?: return
        _isLoading.value = true
        
        // Trước tiên lấy ID của bộ sưu tập "Default"
        repository.getDefaultCollectionId(userEmail) { collectionId ->
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
        viewModelScope.launch {
            repository.deleteMovieFromLocal(movie)
            // Cập nhật lại danh sách cloud nếu cần hoặc refresh
            fetchCloudMovies()
        }
    }
}
