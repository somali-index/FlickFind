package com.example.flickfind.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.ui.tooling.preview.Preview
import com.example.flickfind.DATALAYER.Room.RoomMovies
import com.example.flickfind.ui.theme.FlickFindTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedMoviesScreen(
    onBack: () -> Unit,
    onMovieClick: (String) -> Unit,
    viewModel: SavedMoviesViewModel = viewModel()
) {
    val savedMovies by viewModel.savedMovies.collectAsState()

    SavedMoviesContent(
        savedMovies = savedMovies,
        onBack = onBack,
        onMovieClick = onMovieClick,
        onDeleteMovie = { viewModel.removeMovie(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedMoviesContent(
    savedMovies: List<RoomMovies>,
    onBack: () -> Unit,
    onMovieClick: (String) -> Unit,
    onDeleteMovie: (RoomMovies) -> Unit
) {
    Scaffold(
        containerColor = Color(0xFF0F0F0F),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1A1A),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                title = { Text("PHIM ĐÃ LƯU") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (savedMovies.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Chưa có phim nào được lưu", color = Color.Gray)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(savedMovies) { movie ->
                    SavedMovieItem(
                        movie = movie,
                        onClick = { onMovieClick(movie.IDMovie) },
                        onDeleteClick = { onDeleteMovie(movie) }
                    )
                }
            }
        }
    }
}

@Composable
fun SavedMovieItem(movie: RoomMovies, onClick: () -> Unit, onDeleteClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = movie.URLimage,
                contentDescription = movie.NameMovie,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.DarkGray),
                contentScale = ContentScale.Crop
            )

            // Nút xóa
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(32.dp)
                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Xóa phim",
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = movie.NameMovie,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SavedMoviesPreview() {
    FlickFindTheme {
        SavedMoviesContent(
            savedMovies = listOf(
                RoomMovies("1", "Inception", "A thief who steals corporate secrets...", "S1", "https://example.com/inception.jpg", "148 min", "1"),
                RoomMovies("2", "The Dark Knight", "When the menace known as the Joker wreaks havoc...", "S1", "https://example.com/tdk.jpg", "152 min", "1"),
                RoomMovies("3", "Interstellar", "A team of explorers travel through a wormhole...", "S1", "https://example.com/interstellar.jpg", "169 min", "1")
            ),
            onBack = {},
            onMovieClick = {},
            onDeleteMovie = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SavedMoviesEmptyPreview() {
    FlickFindTheme {
        SavedMoviesContent(
            savedMovies = emptyList(),
            onBack = {},
            onMovieClick = {},
            onDeleteMovie = {}
        )
    }
}
