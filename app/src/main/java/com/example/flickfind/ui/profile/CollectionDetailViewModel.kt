package com.example.flickfind.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.DATALAYER.Room.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CollectionDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository(
        remote = AppRemote(),
        movieDao = AppDatabase.getDatabase(application).movieDao()
    )

    private val _movies = MutableStateFlow<List<DataMovie>>(emptyList())
    val movies = _movies.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun fetchMoviesInCollection(collectionId: String) {
        _isLoading.update { true }
        repository.getMoviesInCollection(collectionId) { movieList ->
            _movies.update { movieList }
            _isLoading.update { false }
        }
    }
}
