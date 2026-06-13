package com.example.flickfind.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.DAO.DAOMovie
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.DATALAYER.Room.AppDatabase
import com.example.flickfind.DATALAYER.Room.RoomMovies
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SavedMoviesViewModel(application: Application) : AndroidViewModel(application) {
    
    private val movieDao: DAOMovie = AppDatabase.getDatabase(application).movieDao()
    private val repository = Repository(AppRemote(), movieDao)
    private val auth = FirebaseAuth.getInstance()

    private val _savedMovies = MutableStateFlow<List<RoomMovies>>(emptyList())
    val savedMovies: StateFlow<List<RoomMovies>> = _savedMovies.asStateFlow()

    init {
        loadSavedMovies()
    }

    private fun loadSavedMovies() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Lấy dữ liệu từ Firestore dựa trên cấu trúc 2 bảng
            repository.getSavedMoviesFromFirestore(currentUser.uid) { movies ->
                // Chuyển đổi DataMovie sang RoomMovies để hiển thị trên UI hiện tại
                val roomList = movies.map { 
                    RoomMovies(
                        IDMovie = it.IDMovie,
                        NameMovie = it.NameMovie,
                        Description = it.Description,
                        IDStudio = it.IDStudio,
                        URLimage = it.URLimage,
                        TimeOneEP = it.TimeOneEP,
                        NummberEP = it.NummberEP
                    )
                }
                _savedMovies.value = roomList
                
                // Đồng bộ xuống Room để xem offline
                viewModelScope.launch {
                    movieDao.clearAll()
                    roomList.forEach { movieDao.insertMovie(it) }
                }
            }
        }
    }

    fun removeMovie(movie: RoomMovies) {
        viewModelScope.launch {
            // Hiện tại mới chỉ xóa ở Local, bạn có thể bổ sung xóa trên Firestore mapping ở đây
            repository.deleteMovieFromLocal(movie)
            _savedMovies.value = _savedMovies.value.filter { it.IDMovie != movie.IDMovie }
        }
    }
}
