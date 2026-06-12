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

class SavedMoviesViewModel(application: Application) : AndroidViewModel(application) {
    
    private val movieDao: DAOMovie = AppDatabase.getDatabase(application).movieDao()
    private val remote = AppRemote()
    private val repository = Repository(remote, movieDao)

    // Sử dụng DataMovie để đồng bộ với Repository
    val savedMovies: StateFlow<List<DataMovie>> = repository.getAllSavedMoviesFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun removeMovie(movie: DataMovie) {
        viewModelScope.launch {
            repository.deleteMovieFromLocal(movie)
        }
    }
}
