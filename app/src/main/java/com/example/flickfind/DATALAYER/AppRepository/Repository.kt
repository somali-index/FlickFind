package com.example.flickfind.DATALAYER.AppRepository

import com.example.flickfind.DATALAYER.DAO.DAOMovie
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.DATALAYER.Room.RoomMovies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Repository(
    private val remote: AppRemote,
    private val movieDao: DAOMovie
) {
    val db = remote.creatRemoteFS()

    // Hàm này chỉ lấy dữ liệu từ Firebase, không tự động lưu vào Room nữa
    fun getMovies(onResult: (List<DataMovie>) -> Unit) {
        db.collection("MovieData")
            .get()
            .addOnSuccessListener { result ->
                val movieList = mutableListOf<DataMovie>()
                for (document in result) {
                    val movie = document.toObject(DataMovie::class.java)
                    // Gán ID từ document vào movie nếu cần
                    movieList.add(movie)
                }
                onResult(movieList)
            }
    }

    // HÀM MỚI: Chỉ gọi hàm này khi người dùng ấn nút "Lưu"
    fun saveMovieToLocal(movie: DataMovie) {
        CoroutineScope(Dispatchers.IO).launch {
            val roomMovie = RoomMovies(
                IDMovie = movie.IDMovie, // Đảm bảo DataMovie có thuộc tính này
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

    // Lấy danh sách phim đã lưu trong máy
    fun getSavedMovies(onResult: (List<RoomMovies>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val savedList = movieDao.getAllMovies()
            onResult(savedList)
        }
    }

    fun getUserProfile(email: String, onResult: (name: String, avatar: String) -> Unit) {
        db.collection("User")
            .whereEqualTo("Email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    val name = document.getString("UserName") ?: "Người dùng"
                    val avatar = document.getString("avatar") ?: ""
                    onResult(name, avatar)
                } else {
                    onResult("Người dùng mới", "")
                }
            }
            .addOnFailureListener {
                onResult("Lỗi kết nối", "")
            }
    }
}
