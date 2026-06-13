package com.example.flickfind.DATALAYER.AppRepository

import com.example.flickfind.DATALAYER.DAO.DAOMovie
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.DATALAYER.Room.*
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Repository(
    private val remote: AppRemote,
    private val movieDao: DAOMovie
) {
    private val db = remote.creatRemoteFS()

    fun getMovies(onResult: (List<DataMovie>) -> Unit) {
        db.collection("MovieData")
            .get()
            .addOnSuccessListener { result ->
                val remoteMovies = result.toObjects(DataMovie::class.java)
                if (remoteMovies.isNotEmpty()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        saveToLocal(remoteMovies)
                        val finalData = getMoviesFromLocal()
                        withContext(Dispatchers.Main) {
                            onResult(finalData)
                        }
                    }
                } else {
                    loadFromLocal(onResult)
                }
            }
            .addOnFailureListener {
                loadFromLocal(onResult)
            }
    }

    private fun loadFromLocal(onResult: (List<DataMovie>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val localData = getMoviesFromLocal()
            withContext(Dispatchers.Main) {
                onResult(localData)
            }
        }
    }

    private suspend fun getMoviesFromLocal(): List<DataMovie> {
        val movies = movieDao.getAllMovies()
        val moviesWithGenres = movieDao.getMoviesWithGenres()
        val moviesWithStudios = movieDao.getMoviesWithStudios()

        return movies.map { roomMovie ->
            val genres = moviesWithGenres.find { it.movie.IDMovie == roomMovie.IDMovie }?.genres
            val genreString = genres?.joinToString(", ") { it.GenreName } ?: ""

            val studios = moviesWithStudios.find { it.movie.IDMovie == roomMovie.IDMovie }?.studios
            val studioString = studios?.joinToString(", ") { it.StudioName } ?: ""

            DataMovie(
                IDMovie = roomMovie.IDMovie,
                NameMovie = roomMovie.NameMovie,
                Description = roomMovie.Description,
                Category = genreString,
                Studio = studioString,
                URLimage = roomMovie.URLimage,
                TimeOneEP = roomMovie.TimeOneEP,
                NummberEP = roomMovie.NummberEP,
                Year = roomMovie.Year
            )
        }
    }

    private suspend fun saveToLocal(list: List<DataMovie>) {
        list.forEach { data ->
            val roomMovie = RoomMovies(
                IDMovie = data.IDMovie,
                NameMovie = data.NameMovie,
                Description = data.Description,
                IDStudio = data.IDStudio,
                URLimage = data.URLimage,
                TimeOneEP = data.TimeOneEP,
                NummberEP = data.NummberEP,
                Year = data.Year
            )
            movieDao.insertMovie(roomMovie)

            if (data.Category.isNotEmpty()) {
                data.Category.split(",").map { it.trim() }.forEach { name ->
                    val genreId = name.hashCode().toString()
                    movieDao.insertGenres(listOf(RoomGenre(genreId, name)))
                    movieDao.insertMovieGenreCrossRef(MovieGenreCrossRef(data.IDMovie, genreId))
                }
            }

            if (data.Studio.isNotEmpty()) {
                data.Studio.split(",").map { it.trim() }.forEach { name ->
                    val studioId = name.hashCode().toString()
                    movieDao.insertStudios(listOf(RoomStudio(studioId, name)))
                    movieDao.insertMovieStudioCrossRef(MovieStudioCrossRef(data.IDMovie, studioId))
                }
            }
        }
    }

    // Các hàm này giữ lại để không làm lỗi các ViewModel khác đang dùng
    fun getUserProfile(email: String, onResult: (name: String, username: String, avatar: String) -> Unit) {
        db.collection("Users")
            .whereEqualTo("Email", email) // Tìm document có trường Email trùng khớp
            .get()
            .addOnSuccessListener { querySnapshot ->
                val doc = querySnapshot.documents.firstOrNull()
                if (doc != null && doc.exists()) {
                    onResult(
                        doc.getString("UserName") ?: "", 
                        doc.getString("Handle") ?: "", 
                        doc.getString("avatar") ?: ""
                    )
                } else {
                    onResult("", "", "")
                }
            }
            .addOnFailureListener { onResult("", "", "") }
    }

    fun updateUserProfile(email: String, name: String?, username: String?, avatar: String?, onResult: (Boolean) -> Unit) {
        val updates = mutableMapOf<String, Any>()
        name?.let { updates["UserName"] = it }
        username?.let { updates["Handle"] = it }
        avatar?.let { updates["avatar"] = it }

        if (updates.isEmpty()) {
            onResult(true)
            return
        }

        // Sử dụng set với merge để tạo mới nếu document chưa tồn tại (cho lần đăng ký đầu tiên)
        db.collection("Users").document(email).set(updates, SetOptions.merge())
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // --- LOCAL USER MANAGEMENT ---
    fun saveUserToLocal(user: RoomUser) {
        CoroutineScope(Dispatchers.IO).launch { movieDao.insertUser(user) }
    }

    suspend fun getLocalUser(): RoomUser? = withContext(Dispatchers.IO) {
        movieDao.getLocalUser()
    }

    fun clearLocalUser() {
        CoroutineScope(Dispatchers.IO).launch { movieDao.clearLocalUser() }
    }

    fun saveMovieToLocal(movie: DataMovie) {
        CoroutineScope(Dispatchers.IO).launch { saveToLocal(listOf(movie)) }
    }

    suspend fun deleteMovieFromLocal(movie: DataMovie) = withContext(Dispatchers.IO) {
        try {
            movieDao.deleteMovie(
                RoomMovies(
                    IDMovie = movie.IDMovie,
                    NameMovie = movie.NameMovie,
                    Description = movie.Description,
                    IDStudio = movie.IDStudio,
                    URLimage = movie.URLimage,
                    TimeOneEP = movie.TimeOneEP,
                    NummberEP = movie.NummberEP,
                    Year = movie.Year
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAllSavedMoviesFlow(): Flow<List<DataMovie>> = movieDao.getAllMoviesFlow().map { list: List<RoomMovies> ->
        list.map { roomMovie ->
            DataMovie(
                IDMovie = roomMovie.IDMovie,
                NameMovie = roomMovie.NameMovie,
                Description = roomMovie.Description,
                URLimage = roomMovie.URLimage,
                TimeOneEP = roomMovie.TimeOneEP,
                NummberEP = roomMovie.NummberEP,
                Year = roomMovie.Year
            )
        }
    }.flowOn(Dispatchers.IO)

    fun getSavedMovieIdsFlow(): Flow<Set<String>> = movieDao.getAllMoviesFlow().map { list: List<RoomMovies> ->
        list.map { it.IDMovie }.toSet()
    }.flowOn(Dispatchers.IO)

    // --- CLOUD COLLECTIONS LOGIC ---

    /**
     * Lưu phim vào bộ sưu tập mặc định của tài khoản (Đồng bộ Cloud)
     */
    fun saveMovieToAccount(email: String, movie: DataMovie, onResult: (Boolean) -> Unit) {
        // Sử dụng bộ sưu tập "Default" làm nơi lưu trữ chung cho tài khoản
        saveMovieToFirestore(email, movie, "Default", onResult)
    }

    fun saveMovieToFirestore(userId: String, movie: DataMovie, collectionName: String, onResult: (Boolean) -> Unit) {
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
                    onResult(false)
                }
            }
            .addOnFailureListener { onResult(false) }
    }

    private fun linkMovieToCollection(movieId: String, collectionId: String) {
        val mapping = mapOf("IDMovie" to movieId, "IDCollection" to collectionId)
        db.collection("CollectionMovies")
            .whereEqualTo("IDMovie", mapping["IDMovie"])
            .whereEqualTo("IDCollection", mapping["IDCollection"])
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.isEmpty) {
                    db.collection("CollectionMovies").add(mapping)
                }
            }
    }

    fun fetchUserCollections(userId: String, onResult: (List<com.example.flickfind.DATALAYER.DataClass.DataCollection>) -> Unit) {
        db.collection("Collections")
            .whereEqualTo("IDUser", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.mapNotNull { it.toObject(com.example.flickfind.DATALAYER.DataClass.DataCollection::class.java) }
                onResult(list)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    fun getMoviesInCollection(collectionId: String, onResult: (List<DataMovie>) -> Unit) {
        db.collection("CollectionMovies")
            .whereEqualTo("IDCollection", collectionId)
            .get()
            .addOnSuccessListener { snapshot ->
                val movieIds = snapshot.documents.mapNotNull { it.getString("IDMovie") }
                if (movieIds.isEmpty()) {
                    onResult(emptyList())
                    return@addOnSuccessListener
                }

                db.collection("Movies")
                    .whereIn("IDMovie", movieIds)
                    .get()
                    .addOnSuccessListener { movieSnapshot ->
                        val movies = movieSnapshot.documents.mapNotNull { it.toObject(DataMovie::class.java) }
                        onResult(movies)
                    }
                    .addOnFailureListener { onResult(emptyList()) }
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    /**
     * Lấy ID của bộ sưu tập "Default" theo Email người dùng
     */
    fun getDefaultCollectionId(email: String, onResult: (String?) -> Unit) {
        db.collection("Collections")
            .whereEqualTo("IDUser", email)
            .whereEqualTo("CollectionName", "Default")
            .get()
            .addOnSuccessListener { querySnapshot ->
                onResult(querySnapshot.documents.firstOrNull()?.id)
            }
            .addOnFailureListener { onResult(null) }
    }
}
