package com.flickfind.app.ui.home

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.flickfind.app.data.model.Movie
import com.flickfind.app.data.repository.MovieRepository
import com.flickfind.app.utils.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FavoritesScreen(
    onNavigateToDetail: (Int, String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val dataStore = remember { DataStoreManager(context) }
    val repository = remember { MovieRepository() }
    
    var favoriteMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isExporting by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val ids = dataStore.getFavoriteIds()
        val movies = mutableListOf<Movie>()
        for (id in ids) {
            repository.getMovieDetail(id).onSuccess { movies.add(it) }
        }
        favoriteMovies = movies
        isLoading = false
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${favoriteMovies.size} phim đã lưu",
                style = MaterialTheme.typography.titleMedium
            )
            
            if (favoriteMovies.isNotEmpty()) {
                Button(
                    onClick = {
                        isExporting = true
                        coroutineScope.launch {
                            try {
                                val jsonFile = withContext(Dispatchers.IO) {
                                    val jsonArray = JSONArray()
                                    favoriteMovies.forEach { movie ->
                                        val obj = JSONObject().apply {
                                            put("id", movie.id)
                                            put("title", movie.title)
                                            put("original_title", movie.originalTitle)
                                            put("release_year", movie.releaseYear)
                                            put("rating", movie.voteAverage)
                                            put("overview", movie.overview)
                                            put("poster_url", movie.posterUrl)
                                            put("genres", JSONArray(movie.genres.map { it.name }))
                                            put("runtime_minutes", movie.runtime)
                                        }
                                        jsonArray.put(obj)
                                    }

                                    val root = JSONObject().apply {
                                        put("exported_at", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()))
                                        put("total_count", favoriteMovies.size)
                                        put("favorites", jsonArray)
                                    }

                                    val dir = context.getExternalFilesDir(null) ?: context.filesDir
                                    val file = File(dir, "flickfind_favorites.json")
                                    FileWriter(file).use { it.write(root.toString(2)) }
                                    file
                                }
                                Toast.makeText(context, "Đã xuất ${favoriteMovies.size} phim → ${jsonFile.name}", Toast.LENGTH_LONG).show()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Xuất thất bại: ${e.message}", Toast.LENGTH_SHORT).show()
                            } finally {
                                isExporting = false
                            }
                        }
                    },
                    enabled = !isExporting
                ) {
                    Text(if (isExporting) "Đang xuất..." else "Xuất JSON")
                }
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (favoriteMovies.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Chưa có phim yêu thích nào")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(favoriteMovies) { movie ->
                    MovieCard(movie = movie, onClick = { onNavigateToDetail(movie.id, movie.title) })
                }
            }
        }
    }
}
