package com.example.flickfind.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.ui.common.LoadingDialog
import com.example.flickfind.ui.theme.FlickFindTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedMoviesScreen(
    onBack: () -> Unit,
    onMovieClick: (String) -> Unit,
    viewModel: SavedMoviesViewModel = viewModel()
) {
    val savedMovies by viewModel.savedMovies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSlowLoading by viewModel.isSlowLoading.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userMessage) {
        userMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.clearMessage()
        }
    }

    SavedMoviesContent(
        savedMovies = savedMovies,
        isLoading = isLoading,
        isSlowLoading = isSlowLoading,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        onMovieClick = onMovieClick,
        onDeleteMovie = { viewModel.removeMovie(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedMoviesContent(
    savedMovies: List<DataMovie>,
    isLoading: Boolean,
    isSlowLoading: Boolean,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onMovieClick: (String) -> Unit,
    onDeleteMovie: (DataMovie) -> Unit
) {
    Scaffold(
        containerColor = Color(0xFF0F0F0F),
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isLoading && savedMovies.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFFFFD700)
                )
            } else if (savedMovies.isEmpty()) {
                Text(
                    text = "Chưa có phim nào được lưu",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
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

        LoadingDialog(
            visible = isSlowLoading,
            message = "Đang xóa phim khỏi danh sách đã lưu..."
        )
    }
}

@Composable
fun SavedMovieItem(movie: DataMovie, onClick: () -> Unit, onDeleteClick: () -> Unit) {
    androidx.compose.foundation.layout.Column(
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
                DataMovie("1", "Inception", "A thief who steals corporate secrets...", "S1", "https://example.com/inception.jpg", "148 min", "1", "Action", "Warner Bros", "2010"),
                DataMovie("2", "The Dark Knight", "When the menace known as the Joker wreaks havoc...", "S1", "https://example.com/tdk.jpg", "152 min", "1", "Action", "Warner Bros", "2008")
            ),
            isLoading = false,
            isSlowLoading = false,
            snackbarHostState = remember { SnackbarHostState() },
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
            isLoading = false,
            isSlowLoading = false,
            snackbarHostState = remember { SnackbarHostState() },
            onBack = {},
            onMovieClick = {},
            onDeleteMovie = {}
        )
    }
}
