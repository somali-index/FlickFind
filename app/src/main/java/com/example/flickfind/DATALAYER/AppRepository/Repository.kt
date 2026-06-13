package com.example.flickfind.DATALAYER.AppRepository

import com.example.flickfind.DATALAYER.DAO.DAOMovie
import com.example.flickfind.DATALAYER.DataClass.DataCollection
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.DATALAYER.Room.MovieGenreCrossRef
import com.example.flickfind.DATALAYER.Room.MovieStudioCrossRef
import com.example.flickfind.DATALAYER.Room.RoomGenre
import com.example.flickfind.DATALAYER.Room.RoomMovies
import com.example.flickfind.DATALAYER.Room.RoomStudio
import com.example.flickfind.DATALAYER.Room.RoomUser
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Repository(
    private val remote: AppRemote,
    private val movieDao: DAOMovie
) {
    private val db = remote.creatRemoteFS()
    private val auth = remote.creatFirebaseAuth()

    fun getMovies(onResult: (List<DataMovie>) -> Unit) {
        db.collection("MovieData")
            .get()
            .addOnSuccessListener { result ->
                val remoteMovies = result.toObjects(DataMovie::class.java)
                if (remoteMovies.isNotEmpty()) {
                    enrichMovies(remoteMovies) { enrichedMovies ->
                        CoroutineScope(Dispatchers.IO).launch {
                            saveToLocal(enrichedMovies)
                            val finalData = getMoviesFromLocal()
                            withContext(Dispatchers.Main) {
                                onResult(finalData)
                            }
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

    private fun enrichMovies(movies: List<DataMovie>, onResult: (List<DataMovie>) -> Unit) {
        fetchStudioNamesByMovieId { studioNamesByMovieId ->
            fetchGenreNamesByMovieId { genreNamesByMovieId ->
                val enrichedMovies = movies.map { movie ->
                    movie.copy(
                        Studio = studioNamesByMovieId[movie.IDMovie]
                            ?: studioNamesByMovieId[movie.IDStudio]
                            ?: movie.Studio,
                        Category = genreNamesByMovieId[movie.IDMovie] ?: movie.Category
                    )
                }
                onResult(enrichedMovies)
            }
        }
    }

    private fun fetchStudioNamesByMovieId(onResult: (Map<String, String>) -> Unit) {
        db.collection("StudioData")
            .get()
            .addOnSuccessListener { studioSnapshot ->
                val studioNamesById = studioSnapshot.documents.associate { document ->
                    val studioId = document.getString("IDStudio") ?: document.id
                    val studioName = document.getString("StudioName").orEmpty()
                    studioId to studioName
                }

                db.collection("Studio_Movie")
                    .get()
                    .addOnSuccessListener { crossRefSnapshot ->
                        val studioNamesByMovieId = crossRefSnapshot.documents
                            .groupBy { it.getString("IDMovie").orEmpty() }
                            .mapValues { (_, documents) ->
                                documents.mapNotNull { document ->
                                    val studioId = document.getString("IDStudio")
                                    studioNamesById[studioId]
                                }.filter { it.isNotBlank() }
                                    .distinct()
                                    .joinToString(", ")
                            }
                            .filterKeys { it.isNotBlank() }
                        onResult(studioNamesByMovieId)
                    }
                    .addOnFailureListener {
                        onResult(emptyMap())
                    }
            }
            .addOnFailureListener {
                onResult(emptyMap())
            }
    }

    private fun fetchGenreNamesByMovieId(onResult: (Map<String, String>) -> Unit) {
        db.collection("MovieGenre")
            .get()
            .addOnSuccessListener { genreSnapshot ->
                val genreNamesById = genreSnapshot.documents.associate { document ->
                    val genreId = document.getString("GenreID") ?: document.id
                    val genreName = document.getString("GenreName").orEmpty()
                    genreId to genreName
                }

                db.collection("Genre_Movie")
                    .get()
                    .addOnSuccessListener { crossRefSnapshot ->
                        val genreNamesByMovieId = crossRefSnapshot.documents
                            .groupBy { it.getString("IDMovie").orEmpty() }
                            .mapValues { (_, documents) ->
                                documents.mapNotNull { document ->
                                    val genreId = document.getString("GenreID")
                                    genreNamesById[genreId]
                                }.filter { it.isNotBlank() }
                                    .distinct()
                                    .joinToString(", ")
                            }
                            .filterKeys { it.isNotBlank() }
                        onResult(genreNamesByMovieId)
                    }
                    .addOnFailureListener {
                        onResult(emptyMap())
                    }
            }
            .addOnFailureListener {
                onResult(emptyMap())
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
                NummberEP = roomMovie.NummberEP
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
                NummberEP = data.NummberEP
            )
            movieDao.insertMovie(roomMovie)

            if (data.Category.isNotEmpty()) {
                data.Category.split(",").map { it.trim() }.filter { it.isNotEmpty() }.forEach { name ->
                    val genreId = name.hashCode().toString()
                    movieDao.insertGenres(listOf(RoomGenre(genreId, name, "")))
                    movieDao.insertMovieGenreCrossRef(MovieGenreCrossRef(genreId, data.IDMovie))
                }
            }

            if (data.Studio.isNotEmpty()) {
                data.Studio.split(",").map { it.trim() }.filter { it.isNotEmpty() }.forEach { name ->
                    val studioId = name.hashCode().toString()
                    movieDao.insertStudios(listOf(RoomStudio(studioId, name)))
                    movieDao.insertMovieStudioCrossRef(MovieStudioCrossRef(studioId, data.IDMovie))
                }
            }
        }
    }

    fun getUserProfile(email: String, onResult: (name: String, username: String, avatar: String) -> Unit) {
        db.collection("Users")
            .whereEqualTo("Email", email)
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

    fun updateUserProfile(
        email: String,
        name: String?,
        username: String?,
        avatar: String?,
        onResult: (Boolean) -> Unit
    ) {
        val firebaseUser = auth.currentUser
        val userId = firebaseUser?.uid ?: email
        val updates = mutableMapOf<String, Any>(
            "IDUser" to userId,
            "Email" to email
        )
        name?.let { updates["UserName"] = it }
        username?.let { updates["Handle"] = it }
        avatar?.let { updates["avatar"] = it }

        findUserDocumentId(email, userId) { documentId ->
            db.collection("Users")
                .document(documentId ?: userId)
                .set(updates, SetOptions.merge())
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        }
    }

    private fun findUserDocumentId(email: String, userId: String?, onResult: (String?) -> Unit) {
        db.collection("Users")
            .whereEqualTo("Email", email)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                val documentId = snapshot.documents.firstOrNull()?.id
                if (documentId != null) {
                    onResult(documentId)
                } else if (!userId.isNullOrBlank()) {
                    db.collection("Users")
                        .whereEqualTo("IDUser", userId)
                        .limit(1)
                        .get()
                        .addOnSuccessListener { idSnapshot ->
                            onResult(idSnapshot.documents.firstOrNull()?.id)
                        }
                        .addOnFailureListener { onResult(null) }
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener { onResult(null) }
    }

    private fun resolveUserId(userKey: String?, onResult: (String?) -> Unit) {
        val key = userKey?.trim().orEmpty()
        val currentUser = auth.currentUser

        if (key.isNotEmpty() && !key.contains("@")) {
            onResult(key)
            return
        }

        val email = key.ifEmpty { currentUser?.email.orEmpty() }
        if (email.isBlank()) {
            onResult(currentUser?.uid)
            return
        }

        db.collection("Users")
            .whereEqualTo("Email", email)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                val userId = snapshot.documents.firstOrNull()?.getString("IDUser")
                onResult(userId ?: currentUser?.uid)
            }
            .addOnFailureListener { onResult(currentUser?.uid) }
    }

    fun getCurrentUserId(onResult: (String?) -> Unit) {
        resolveUserId(null, onResult)
    }

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
                    NummberEP = movie.NummberEP
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
                NummberEP = roomMovie.NummberEP
            )
        }
    }.flowOn(Dispatchers.IO)

    fun getSavedMovieIdsFlow(): Flow<Set<String>> = movieDao.getAllMoviesFlow().map { list: List<RoomMovies> ->
        list.map { it.IDMovie }.toSet()
    }.flowOn(Dispatchers.IO)

    fun saveMovieToAccount(userKey: String, movie: DataMovie, onResult: (Boolean) -> Unit) {
        resolveUserId(userKey) { userId ->
            if (userId == null) {
                onResult(false)
            } else {
                saveMovieToCollectionByUserId(userId, movie, "Default", createIfMissing = true, onResult)
            }
        }
    }

    fun saveCurrentUserMovieToAccount(movie: DataMovie, onResult: (Boolean) -> Unit) {
        saveMovieToAccount("", movie, onResult)
    }

    fun saveMovieToFirestore(userKey: String, movie: DataMovie, collectionName: String, onResult: (Boolean) -> Unit) {
        resolveUserId(userKey) { userId ->
            if (userId == null) {
                onResult(false)
            } else {
                saveMovieToCollectionByUserId(userId, movie, collectionName, createIfMissing = false, onResult)
            }
        }
    }

    fun saveCurrentUserMovieToCollection(movie: DataMovie, collectionName: String, onResult: (Boolean) -> Unit) {
        saveMovieToFirestore("", movie, collectionName, onResult)
    }

    private fun saveMovieToCollectionByUserId(
        userId: String,
        movie: DataMovie,
        collectionName: String,
        createIfMissing: Boolean,
        onResult: (Boolean) -> Unit
    ) {
        findCollection(userId, collectionName) { collection ->
            if (collection != null) {
                linkMovieToCollection(movie.IDMovie, collection.IDCollection, onResult)
            } else if (createIfMissing) {
                createCollectionForUser(userId, collectionName) { created ->
                    if (created != null) {
                        linkMovieToCollection(movie.IDMovie, created.IDCollection, onResult)
                    } else {
                        onResult(false)
                    }
                }
            } else {
                onResult(false)
            }
        }
    }

    private fun findCollection(userId: String, collectionName: String, onResult: (DataCollection?) -> Unit) {
        db.collection("Collections")
            .whereEqualTo("IDUser", userId)
            .whereEqualTo("CollectionName", collectionName)
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                onResult(querySnapshot.documents.firstOrNull()?.toObject(DataCollection::class.java))
            }
            .addOnFailureListener { onResult(null) }
    }

    fun createCollectionForCurrentUser(collectionName: String, onResult: (Boolean) -> Unit) {
        resolveUserId(null) { userId ->
            if (userId == null) {
                onResult(false)
            } else {
                createCollectionForUser(userId, collectionName) { created ->
                    onResult(created != null)
                }
            }
        }
    }

    fun updateCollectionName(collectionId: String, newName: String, onResult: (Boolean) -> Unit) {
        db.collection("Collections")
            .whereEqualTo("IDCollection", collectionId)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                val document = snapshot.documents.firstOrNull()
                if (document == null) {
                    onResult(false)
                    return@addOnSuccessListener
                }

                document.reference
                    .set(mapOf("CollectionName" to newName.trim()), SetOptions.merge())
                    .addOnSuccessListener { onResult(true) }
                    .addOnFailureListener { onResult(false) }
            }
            .addOnFailureListener { onResult(false) }
    }

    fun deleteCollection(collectionId: String, onResult: (Boolean) -> Unit) {
        db.collection("CollectionMovies")
            .whereEqualTo("IDCollection", collectionId)
            .get()
            .addOnSuccessListener { mappingSnapshot ->
                val mappingRefs = mappingSnapshot.documents.map { it.reference }

                db.collection("Collections")
                    .whereEqualTo("IDCollection", collectionId)
                    .limit(1)
                    .get()
                    .addOnSuccessListener { collectionSnapshot ->
                        val collectionRef = collectionSnapshot.documents.firstOrNull()?.reference
                        if (collectionRef == null) {
                            onResult(false)
                            return@addOnSuccessListener
                        }

                        val batch = db.batch()
                        mappingRefs.forEach { batch.delete(it) }
                        batch.delete(collectionRef)
                        batch.commit()
                            .addOnSuccessListener { onResult(true) }
                            .addOnFailureListener { onResult(false) }
                    }
                    .addOnFailureListener { onResult(false) }
            }
            .addOnFailureListener { onResult(false) }
    }

    private fun createCollectionForUser(
        userId: String,
        collectionName: String,
        onResult: (DataCollection?) -> Unit
    ) {
        findCollection(userId, collectionName) { existing ->
            if (existing != null) {
                onResult(existing)
                return@findCollection
            }

            getNextNumericId("Collections", "IDCollection") { nextId ->
                if (nextId == null) {
                    onResult(null)
                    return@getNextNumericId
                }

                val newCollection = DataCollection(
                    IDCollection = nextId,
                    IDUser = userId,
                    CollectionName = collectionName.trim()
                )

                db.collection("Collections")
                    .document(nextId)
                    .set(newCollection)
                    .addOnSuccessListener { onResult(newCollection) }
                    .addOnFailureListener { onResult(null) }
            }
        }
    }

    private fun linkMovieToCollection(movieId: String, collectionId: String, onResult: (Boolean) -> Unit) {
        val mapping = mapOf(
            "IDMovie" to movieId,
            "IDCollection" to collectionId
        )

        db.collection("CollectionMovies")
            .whereEqualTo("IDMovie", movieId)
            .whereEqualTo("IDCollection", collectionId)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    onResult(true)
                    return@addOnSuccessListener
                }

                getNextNumericId("CollectionMovies", null) { nextId ->
                    if (nextId == null) {
                        onResult(false)
                        return@getNextNumericId
                    }

                    db.collection("CollectionMovies")
                        .document(nextId)
                        .set(mapping)
                        .addOnSuccessListener { onResult(true) }
                        .addOnFailureListener { onResult(false) }
                }
            }
            .addOnFailureListener { onResult(false) }
    }

    fun fetchUserCollections(userKey: String, onResult: (List<DataCollection>) -> Unit) {
        resolveUserId(userKey) { userId ->
            if (userId == null) {
                onResult(emptyList())
                return@resolveUserId
            }

            db.collection("Collections")
                .whereEqualTo("IDUser", userId)
                .get()
                .addOnSuccessListener { snapshot ->
                    val list = snapshot.documents
                        .mapNotNull { it.toObject(DataCollection::class.java) }
                        .sortedBy { it.IDCollection.toIntOrNull() ?: Int.MAX_VALUE }
                    onResult(list)
                }
                .addOnFailureListener { onResult(emptyList()) }
        }
    }

    fun fetchCurrentUserCollections(onResult: (List<DataCollection>) -> Unit) {
        fetchUserCollections("", onResult)
    }

    fun listenCurrentUserCollections(onResult: (List<DataCollection>) -> Unit) {
        resolveUserId(null) { userId ->
            if (userId == null) {
                onResult(emptyList())
                return@resolveUserId
            }

            db.collection("Collections")
                .whereEqualTo("IDUser", userId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        onResult(emptyList())
                        return@addSnapshotListener
                    }

                    val list = snapshot?.documents
                        ?.mapNotNull { it.toObject(DataCollection::class.java) }
                        ?.sortedBy { it.IDCollection.toIntOrNull() ?: Int.MAX_VALUE }
                        ?: emptyList()
                    onResult(list)
                }
        }
    }

    fun getMoviesInCollection(collectionId: String, onResult: (List<DataMovie>) -> Unit) {
        db.collection("CollectionMovies")
            .whereEqualTo("IDCollection", collectionId)
            .get()
            .addOnSuccessListener { snapshot ->
                val movieIds = snapshot.documents.mapNotNull { it.getString("IDMovie") }.distinct()
                if (movieIds.isEmpty()) {
                    onResult(emptyList())
                    return@addOnSuccessListener
                }

                val chunks = movieIds.chunked(10)
                val movies = mutableListOf<DataMovie>()
                var completed = 0
                var failed = false

                chunks.forEach { chunk ->
                    db.collection("MovieData")
                        .whereIn("IDMovie", chunk)
                        .get()
                        .addOnSuccessListener { movieSnapshot ->
                            movies.addAll(movieSnapshot.documents.mapNotNull { it.toObject(DataMovie::class.java) })
                            completed += 1
                            if (completed == chunks.size && !failed) {
                                onResult(movies.distinctBy { it.IDMovie })
                            }
                        }
                        .addOnFailureListener {
                            failed = true
                            onResult(emptyList())
                        }
                }
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    fun getDefaultCollectionId(userKey: String, onResult: (String?) -> Unit) {
        resolveUserId(userKey) { userId ->
            if (userId == null) {
                onResult(null)
                return@resolveUserId
            }

            findCollection(userId, "Default") { collection ->
                onResult(collection?.IDCollection)
            }
        }
    }

    fun getCurrentDefaultCollectionId(onResult: (String?) -> Unit) {
        getDefaultCollectionId("", onResult)
    }

    fun removeMovieFromCurrentAccount(movieId: String, onResult: (Boolean) -> Unit) {
        getCurrentDefaultCollectionId { collectionId ->
            if (collectionId == null) {
                onResult(true)
                return@getCurrentDefaultCollectionId
            }

            db.collection("CollectionMovies")
                .whereEqualTo("IDCollection", collectionId)
                .whereEqualTo("IDMovie", movieId)
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.isEmpty) {
                        onResult(true)
                        return@addOnSuccessListener
                    }

                    var completed = 0
                    var failed = false
                    snapshot.documents.forEach { document ->
                        document.reference.delete()
                            .addOnSuccessListener {
                                completed += 1
                                if (completed == snapshot.size() && !failed) {
                                    onResult(true)
                                }
                            }
                            .addOnFailureListener {
                                failed = true
                                onResult(false)
                            }
                    }
                }
                .addOnFailureListener {
                    onResult(false)
                }
        }
    }

    private fun getNextNumericId(collectionName: String, idField: String?, onResult: (String?) -> Unit) {
        db.collection(collectionName)
            .get()
            .addOnSuccessListener { snapshot ->
                val maxId = snapshot.maxNumericId(idField)
                onResult((maxId + 1).toString())
            }
            .addOnFailureListener { onResult(null) }
    }

    private fun QuerySnapshot.maxNumericId(idField: String?): Int {
        return documents.maxOfOrNull { document ->
            val fieldValue = idField?.let { field ->
                document.getString(field) ?: document.getLong(field)?.toString()
            }
            val value = fieldValue ?: document.id
            value.toIntOrNull() ?: 0
        } ?: 0
    }
}
