package com.example.flickfind.ui.home

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.DATALAYER.DataClass.DataCollection
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class HomeUiState(
    val movieList: List<DataMovie> = emptyList(),
    val savedMovieIds: Set<String> = emptySet(),
    val collections: List<DataCollection> = emptyList(),
    val isLoading: Boolean = true,
    val userMessage: String? = null
)

class HomeViewModel(application: android.app.Application) : AndroidViewModel(application) {

    private val repository = Repository(
        remote = AppRemote(),
        movieDao = com.example.flickfind.DATALAYER.Room.AppDatabase.getDatabase(application).movieDao()
    )

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()

    // Bộ lọc thư mục rác (Đồng bộ với logic Profile)
    private val forbiddenNames = listOf("Danh sách đã lưu", "LƯU NHANH", "Quick Save", "Saved Movies", "Default")

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

    /**
     * TÍNH NĂNG: LƯU NHANH (Local)
     * Chỉ lưu vào máy (Room), không đẩy lên Firestore.
     */
    fun saveMovie(movie: DataMovie) {
        repository.saveMovieToLocal(movie)
        _homeUiState.update { it.copy(userMessage = "Đã lưu nhanh '${movie.NameMovie}' vào máy") }
    }

    /**
     * TÍNH NĂNG: LƯU VÀO TÀI KHOẢN (Đồng bộ Cloud - Bộ sưu tập mặc định)
     */
    fun saveMovieToAccount(movie: DataMovie) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            repository.saveMovieToAccount(currentUser.email ?: "", movie) { success ->
                val message = if (success) {
                    "Đã đồng bộ '${movie.NameMovie}' vào tài khoản"
                } else {
                    "Lỗi khi đồng bộ vào tài khoản"
                }
                _homeUiState.update { it.copy(userMessage = message) }
            }
        } else {
            _homeUiState.update { it.copy(userMessage = "Vui lòng đăng nhập để đồng bộ") }
        }
    }

    /**
     * TÍNH NĂNG: LƯU VÀO BỘ SƯU TẬP (Cloud)
     * Lưu thông qua Repository để đảm bảo đúng cấu trúc Firestore.
     */
    fun saveMovieToCollection(movie: DataMovie, collectionName: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            repository.saveMovieToFirestore(currentUser.email ?: "", movie, collectionName) { success ->
                val message = if (success) {
                    "Đã thêm '${movie.NameMovie}' vào bộ sưu tập '$collectionName'"
                } else {
                    "Lỗi: Không tìm thấy bộ sưu tập '$collectionName'"
                }
                _homeUiState.update { it.copy(userMessage = message) }
            }
        } else {
            _homeUiState.update { it.copy(userMessage = "Vui lòng đăng nhập để lưu phim") }
        }
    }

    /**
     * Lấy danh sách bộ sưu tập hiện có (có lọc rác).
     */
    fun fetchUserCollections() {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        repository.fetchUserCollections(currentUser.email ?: "") { list ->
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
}
