package com.example.flickfind.ui.home

import androidx.lifecycle.AndroidViewModel
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.DATALAYER.DataClass.DataCollection
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class HomeUiState(
    val movieList: List<DataMovie> = emptyList(),
    val collections: List<DataCollection> = emptyList(),
    val isLoading: Boolean = true,
    val userMessage: String? = null
)

class HomeViewModel(application: android.app.Application) : AndroidViewModel(application) {

    private val repository = Repository(
        remote = AppRemote(),
        movieDao = com.example.flickfind.DATALAYER.Room.AppDatabase.getDatabase(application).movieDao()
    )

    // STATE
    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()

    // CACHE
    private var allMoviesHome: List<DataMovie> = emptyList()

    init {
        getMovieListHome()
    }

    fun saveMovie(movie: DataMovie) {
        val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // Lưu nhanh chỉ lưu vào Local (Room) theo đúng phân cấp dữ liệu đã thống nhất
            repository.saveMovieToLocal(movie)
            _homeUiState.update { it.copy(userMessage = "Đã lưu nhanh '${movie.NameMovie}' vào máy") }
        } else {
            _homeUiState.update { it.copy(userMessage = "Vui lòng đăng nhập để lưu phim") }
        }
    }

    fun saveMovieToCollection(movie: DataMovie, collectionId: String, collectionName: String) {
        val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val db = FirebaseFirestore.getInstance()
            val mapping = mapOf(
                "IDMovie" to movie.IDMovie,
                "IDCollection" to collectionId
            )
            
            db.collection("CollectionMovies")
                .whereEqualTo("IDMovie", movie.IDMovie)
                .whereEqualTo("IDCollection", collectionId)
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.isEmpty) {
                        db.collection("CollectionMovies").add(mapping)
                            .addOnSuccessListener {
                                _homeUiState.update { it.copy(userMessage = "Đã thêm '${movie.NameMovie}' vào bộ sưu tập '$collectionName'") }
                            }
                    } else {
                        _homeUiState.update { it.copy(userMessage = "Phim đã có trong bộ sưu tập '$collectionName'") }
                    }
                }
        }
    }

    fun fetchUserCollections() {
        val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser ?: return
        val db = FirebaseFirestore.getInstance()
        
        db.collection("Collections")
            .whereEqualTo("IDUser", currentUser.uid)
            .get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.mapNotNull { it.toObject(DataCollection::class.java) }
                _homeUiState.update { it.copy(collections = list) }
            }
    }

    fun clearMessage() {
        _homeUiState.update { it.copy(userMessage = null) }
    }

    fun logout() {
        repository.clearLocalUser()
        com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
    }

    private fun getMovieListHome() {
        repository.getMovies { movieList ->
            allMoviesHome = movieList
            _homeUiState.update {
                it.copy(
                    movieList = movieList,
                    isLoading = false
                )
            }
        }
    }
}
