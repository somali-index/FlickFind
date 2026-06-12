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
    fun updateUserProfile(email: String, name: String?, username: String?, avatar: String?, onResult: (Boolean) -> Unit) {
        val userMap = mutableMapOf<String, Any>()
        name?.let { userMap["name"] = it }
        username?.let { userMap["username"] = it }
        avatar?.let { userMap["avatar"] = it }
        db.collection("Users").document(email).set(userMap, SetOptions.merge())
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun getUserProfile(email: String, onResult: (name: String, username: String, avatar: String) -> Unit) {
        db.collection("Users").document(email).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    onResult(doc.getString("name") ?: "", doc.getString("username") ?: "", doc.getString("avatar") ?: "")
                } else onResult("", "", "")
            }
            .addOnFailureListener { onResult("", "", "") }
    }

    fun saveMovieToLocal(movie: DataMovie) {
        CoroutineScope(Dispatchers.IO).launch { saveToLocal(listOf(movie)) }
    }

    fun deleteMovieFromLocal(movie: DataMovie) {
        CoroutineScope(Dispatchers.IO).launch {
            movieDao.deleteMovie(RoomMovies(movie.IDMovie, movie.NameMovie, movie.Description, movie.IDStudio, movie.URLimage, movie.TimeOneEP, movie.NummberEP, movie.Year))
        }
    }

    fun getAllSavedMoviesFlow(): Flow<List<DataMovie>> = flow { emit(getMoviesFromLocal()) }.flowOn(Dispatchers.IO)
}
