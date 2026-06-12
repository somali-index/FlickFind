package com.example.flickfind.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.DAO.DAOMovie
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.DATALAYER.Room.AppDatabase
import com.example.flickfind.DATALAYER.Room.RoomMovies
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SavedMoviesViewModel(application: Application) : AndroidViewModel(application) {
    
    private val movieDao: DAOMovie = AppDatabase.getDatabase(application).movieDao()
    private val remote = AppRemote()
    // Khởi tạo Repository (Lưu ý: Trong thực tế nên dùng Dependency Injection)
    private val repository = Repository(remote, movieDao)

    // Sử dụng Flow từ Repository và chuyển đổi sang StateFlow để UI quan sát
    val savedMovies: StateFlow<List<RoomMovies>> = repository.getAllSavedMoviesFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun removeMovie(movie: RoomMovies) {
        viewModelScope.launch {
            repository.deleteMovieFromLocal(movie)
            // Vì chúng ta đang quan sát Flow, UI sẽ tự động cập nhật khi phim bị xóa
        }
    }
}
