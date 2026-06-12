package com.example.flickfind.DATALAYER.AppRepository

import com.example.flickfind.DATALAYER.DAO.DAOMovie
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.DATALAYER.Room.RoomMovies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

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
    fun getUserProfile(email: String, onResult: (name: String, username: String, avatar: String) -> Unit) {
        db.collection("User")
            .whereEqualTo("Email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    val name = document.getString("UserName") ?: "Người dùng"
                    val username = document.getString("Handle") ?: ""
                    val avatar = document.getString("avatar") ?: ""
                    onResult(name, username, avatar)
                } else {
                    onResult("Người dùng mới", "", "")
                }
            }
            .addOnFailureListener {
                onResult("Lỗi kết nối", "", "")
            }
    }

    // Cập nhật thông tin Profile người dùng
    fun updateUserProfile(email: String, name: String?, username: String?, avatar: String?, onResult: (Boolean) -> Unit) {
        db.collection("User")
            .whereEqualTo("Email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val documentId = querySnapshot.documents[0].id
                    val updates = mutableMapOf<String, Any>()
                    if (name != null) updates["UserName"] = name
                    if (username != null) updates["Handle"] = username
                    if (avatar != null) updates["avatar"] = avatar

                    db.collection("User").document(documentId)
                        .update(updates)
                        .addOnSuccessListener { onResult(true) }
                        .addOnFailureListener { onResult(false) }
                } else {
                    // Nếu chưa có document thì tạo mới
                    val newUser = mutableMapOf<String, Any>("Email" to email)
                    if (name != null) newUser["UserName"] = name
                    if (username != null) newUser["Handle"] = username
                    if (avatar != null) newUser["avatar"] = avatar

                    db.collection("User").add(newUser)
                        .addOnSuccessListener { onResult(true) }
                        .addOnFailureListener { onResult(false) }
                }
            }
            .addOnFailureListener { onResult(false) }
    }
}
