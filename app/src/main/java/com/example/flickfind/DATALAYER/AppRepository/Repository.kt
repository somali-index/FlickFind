package com.example.flickfind.DATALAYER.AppRepository

import androidx.compose.ui.tooling.preview.Preview
import com.example.flickfind.DATALAYER.DAO.DAOMovie
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.DATALAYER.Room.RoomMovies
import com.example.flickfind.DATALAYER.Room.RoomUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class Repository(
    private val remote: AppRemote,
    private val movieDao: DAOMovie
) {
    val db = remote.creatRemoteFS()

    // Lấy danh sách phim từ Firebase Firestore
    fun getMovies(onResult: (List<DataMovie>) -> Unit) {
        db.collection("MovieData")
            .get()
            .addOnSuccessListener { result ->
                val movieList = mutableListOf<DataMovie>()
                for (document in result) {
                    val movie = document.toObject(DataMovie::class.java)
                    movieList.add(movie)
                }
                onResult(movieList)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    // Lưu phim vào Room Database (Local)
    fun saveMovieToLocal(movie: DataMovie) {
        CoroutineScope(Dispatchers.IO).launch {
            val roomMovie = RoomMovies(
                IDMovie = movie.IDMovie,
                NameMovie = movie.NameMovie,
                Description = movie.Description,
                IDStudio = movie.IDStudio,
                URLimage = movie.URLimage,
                TimeOneEP = movie.TimeOneEP,
                NummberEP = movie.NummberEP
            )
            movieDao.insertMovie(roomMovie)
        }
    }

    // Xóa phim khỏi Room Database (Local)
    fun deleteMovieFromLocal(movie: RoomMovies) {
        CoroutineScope(Dispatchers.IO).launch {
            movieDao.deleteMovie(movie)
        }
    }

    // Lấy danh sách phim đã lưu dưới dạng Flow để cập nhật UI tự động (Khuyên dùng)
    fun getAllSavedMoviesFlow(): Flow<List<RoomMovies>> {
        return movieDao.getAllMoviesFlow()
    }

    // Lấy danh sách phim đã lưu (Dạng suspend cho các tác vụ xử lý một lần)
    suspend fun getSavedMoviesList(): List<RoomMovies> {
        return movieDao.getAllMovies()
    }

    // Lấy thông tin Profile người dùng từ Firestore
    fun getUserProfile(email: String, onResult: (name: String, avatar: String) -> Unit) {
        db.collection("User")
            .whereEqualTo("Email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    val name = document.getString("UserName") ?: "Người dùng"
                    val avatar = document.getString("avatar") ?: ""
                    
                    // Lưu vào Room khi lấy được dữ liệu từ Firestore
                    val idUser = document.getString("IDUser") ?: ""
                    val pass = document.getString("Pass") ?: ""
                    saveUserToLocal(RoomUser(idUser, email, pass, name, avatar))
                    
                    onResult(name, avatar)
                } else {
                    onResult("Người dùng mới", "")
                }
            }
            .addOnFailureListener {
                onResult("Lỗi kết nối", "")
            }
    }

    // --- QUẢN LÝ USER TRONG ROOM ---
    
    fun saveUserToLocal(user: RoomUser) {
        CoroutineScope(Dispatchers.IO).launch {
            movieDao.insertUser(user)
        }
    }

    suspend fun getLocalUser(): RoomUser? {
        return withContext(Dispatchers.IO) {
            movieDao.getUser()
        }
    }

    fun clearLocalUser() {
        CoroutineScope(Dispatchers.IO).launch {
            movieDao.clearUser()
        }
    }

    // --- QUẢN LÝ COLLECTIONS TRÊN FIRESTORE ---

    // 1. Lưu phim vào Firestore theo cấu trúc 2 bảng
    fun saveMovieToFirestore(userId: String, movie: DataMovie, collectionName: String, onResult: (Boolean) -> Unit = {}) {
        db.collection("Collections")
            .whereEqualTo("IDUser", userId)
            .whereEqualTo("CollectionName", collectionName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val collectionId = querySnapshot.documents[0].id
                    linkMovieToCollection(movie.IDMovie, collectionId)
                    onResult(true)
                } else {
                    // Nếu không tìm thấy bộ sưu tập, báo về để UI hiện thông báo (KHÔNG tự tạo rác)
                    onResult(false)
                }
            }
            .addOnFailureListener { onResult(false) }
    }

    private fun linkMovieToCollection(movieId: String, collectionId: String) {
        // Kiểm tra xem phim đã có trong collection chưa
        db.collection("CollectionMovies")
            .whereEqualTo("IDMovie", movieId)
            .whereEqualTo("IDCollection", collectionId)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.isEmpty) {
                    val mapping = mapOf(
                        "IDMovie" to movieId,
                        "IDCollection" to collectionId
                    )
                    db.collection("CollectionMovies").add(mapping)
                }
            }
    }

    // 2. Lấy danh sách phim đã lưu từ Firestore dựa trên 2 bảng
    fun getSavedMoviesFromFirestore(userId: String, onResult: (List<DataMovie>) -> Unit) {
        // B1: Lấy IDCollection của User
        db.collection("Collections")
            .whereEqualTo("IDUser", userId)
            .get()
            .addOnSuccessListener { collectionSnapshot ->
                if (collectionSnapshot.isEmpty) {
                    onResult(emptyList())
                    return@addOnSuccessListener
                }

                val collectionIds = collectionSnapshot.documents.map { it.id }
                
                // B2: Lấy danh sách IDMovie từ CollectionMovies
                db.collection("CollectionMovies")
                    .whereIn("IDCollection", collectionIds)
                    .get()
                    .addOnSuccessListener { mappingSnapshot ->
                        val movieIds = mappingSnapshot.documents.mapNotNull { it.getString("IDMovie") }
                        if (movieIds.isEmpty()) {
                            onResult(emptyList())
                            return@addOnSuccessListener
                        }

                        // B3: Lấy chi tiết phim từ MovieData
                        db.collection("MovieData")
                            .whereIn("IDMovie", movieIds)
                            .get()
                            .addOnSuccessListener { movieSnapshot ->
                                val movies = movieSnapshot.documents.mapNotNull { it.toObject(DataMovie::class.java) }
                                onResult(movies)
                            }
                    }
            }
            .addOnFailureListener { onResult(emptyList()) }
    }
}
