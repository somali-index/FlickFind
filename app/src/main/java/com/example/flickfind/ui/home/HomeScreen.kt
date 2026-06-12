package com.example.flickfind.ui.home

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.ui.tooling.preview.Preview
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.ui.SearchUI.SearchActivity
import com.example.flickfind.ui.theme.FlickFindTheme

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onProfileClick: () -> Unit,
    onMovieClick: (String) -> Unit,
    onSettingsClick: () -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.homeUiState.collectAsState()

    HomeScreenBody(
        uiState = uiState,
        onLogout = onLogout,
        onProfileClick = onProfileClick,
        onMovieClick = onMovieClick,
        onSettingsClick = onSettingsClick,
        onSearchClick = {
            context.startActivity(Intent(context, SearchActivity::class.java))
        },
        onSaveClick = { movie ->
            viewModel.saveMovie(movie)
        },
        onClearMessage = {
            viewModel.clearMessage()
        }
    )
}

@Composable
private fun HomeScreenBody(
    uiState: HomeUiState,
    onLogout: () -> Unit,
    onProfileClick: () -> Unit,
    onMovieClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSaveClick: (DataMovie) -> Unit,
    onClearMessage: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Xử lý hiển thị thông báo qua Snackbar
    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            onClearMessage()
        }
    }

    HomeScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onLogout = onLogout,
        onProfileClick = onProfileClick,
        onSettingsClick = onSettingsClick,
        onSearchClick = onSearchClick,
        onSaveClick = onSaveClick,
        onMovieClick = onMovieClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    uiState: HomeUiState,
    snackbarHostState: SnackbarHostState,
    onLogout: () -> Unit,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSaveClick: (DataMovie) -> Unit,
    onMovieClick: (String) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Movie, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text("FlickFind", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Default.Search, "Search")
                    }
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.Person, "Menu", tint = MaterialTheme.colorScheme.primary)
                        }
                        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                            DropdownMenuItem(text = { Text("👤 Thông tin cá nhân") }, onClick = { showMenu = false; onProfileClick() })
                            HorizontalDivider()
                            DropdownMenuItem(text = { Text("🚪 Đăng xuất", color = Color.Red) }, onClick = { showMenu = false; onLogout() })
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                Modifier.fillMaxSize().background(Color(0xFF0F0F0F)).padding(paddingValues),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item { HomeHeader() }
                item { MovieSection("🎬 Danh sách phim", uiState.movieList, onSaveClick, onMovieClick) }
                item { MovieSection("🎬 Phim hot", uiState.movieList, onSaveClick, onMovieClick) }
            }
        }
    }
}

@Composable
fun HomeHeader() {
    Column(Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 8.dp)) {
        Text("Khám phá thế giới phim cùng", color = Color.Gray, fontSize = 14.sp)
        Text("FlickFind", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun MovieSection(title: String, movies: List<DataMovie>, onSaveClick: (DataMovie) -> Unit, onMovieClick: (String) -> Unit) {
    Column(Modifier.padding(top = 20.dp)) {
        Text(title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 16.dp, bottom = 12.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(movies) { movie -> MovieCard(movie, { onSaveClick(movie) }, { onMovieClick(movie.IDMovie) }) }
        }
    }
}

@Composable
fun MovieCard(movie: DataMovie, onSaveClick: () -> Unit, onMovieClick: () -> Unit) {
    Column(Modifier.width(120.dp).clickable { onMovieClick() }) {
        Card(
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.width(120.dp).height(180.dp)
        ) {
            Box(Modifier.fillMaxSize()) {
                AsyncImage(model = movie.URLimage, contentDescription = movie.NameMovie, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                IconButton(
                    onClick = onSaveClick,
                    modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).size(32.dp).background(Color.Black.copy(0.5f), CircleShape)
                ) {
                    Icon(Icons.Default.Bookmark, "Save", tint = Color(0xFF00E5FF), modifier = Modifier.size(18.dp))
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(movie.NameMovie, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Text(movie.Description, color = Color.Gray, fontSize = 11.sp, maxLines = 1)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    FlickFindTheme {
        Surface(color = Color(0xFF0F0F0F)) {
            HomeScreenBody(
                uiState = HomeUiState(
                    movieList = listOf(
                        DataMovie(
                            NameMovie = "Avengers: Endgame",
                            Description = "The remaining Avengers must gather their allies and take a stand.",
                            URLimage = "https://example.com/endgame.jpg"
                        ),
                        DataMovie(
                            NameMovie = "Inception",
                            Description = "A thief who steals corporate secrets through the use of dream-sharing technology.",
                            URLimage = "https://example.com/inception.jpg"
                        )
                    ),
                    isLoading = false
                ),
                onLogout = {},
                onProfileClick = {},
                onMovieClick = {},
                onSettingsClick = {},
                onSearchClick = {},
                onSaveClick = {},
                onClearMessage = {}
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F0F)
@Composable
fun HomeHeaderPreview() {
    FlickFindTheme {
        HomeHeader()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F0F)
@Composable
fun MovieSectionPreview() {
    val sampleMovies = listOf(
        DataMovie(NameMovie = "Avengers: Endgame", Description = "The remaining Avengers..."),
        DataMovie(NameMovie = "Inception", Description = "A thief who steals...")
    )
    FlickFindTheme {
        MovieSection(
            title = "🎬 Danh sách phim",
            movies = sampleMovies,
            onSaveClick = {},
            onMovieClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F0F)
@Composable
fun MovieCardPreview() {
    FlickFindTheme {
        MovieCard(
            movie = DataMovie(
                NameMovie = "Avengers: Endgame",
                Description = "The remaining Avengers must gather their allies..."
            ),
            onSaveClick = {},
            onMovieClick = {}
        )
    }
}
