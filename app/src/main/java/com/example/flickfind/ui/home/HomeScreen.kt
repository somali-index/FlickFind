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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.ui.tooling.preview.Preview
import com.example.flickfind.DATALAYER.DataClass.DataCollection
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.ui.SearchUI.SearchActivity
import com.example.flickfind.ui.theme.FlickFindTheme
import kotlinx.coroutines.launch

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
        onSaveLocal = { movie ->
            viewModel.saveMovie(movie)
        },
        onSaveToAccount = { movie ->
            viewModel.saveMovieToAccount(movie)
        },
        onSaveToCollection = { movie, collectionName ->
            viewModel.saveMovieToCollection(movie, collectionName)
        },
        onFetchCollections = {
            viewModel.fetchUserCollections()
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
    onSaveLocal: (DataMovie) -> Unit,
    onSaveToAccount: (DataMovie) -> Unit,
    onSaveToCollection: (DataMovie, String) -> Unit,
    onFetchCollections: () -> Unit,
    onClearMessage: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedMovieForSave by remember { mutableStateOf<DataMovie?>(null) }
    val scope = rememberCoroutineScope()

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
        onSaveClick = { movie -> 
            selectedMovieForSave = movie
            onFetchCollections()
        },
        onMovieClick = onMovieClick
    )

    if (selectedMovieForSave != null) {
        SaveOptionsBottomSheet(
            movie = selectedMovieForSave!!,
            collections = uiState.collections,
            onDismiss = { selectedMovieForSave = null },
            onSaveLocal = {
                onSaveLocal(it)
                selectedMovieForSave = null
            },
            onSaveToAccount = {
                onSaveToAccount(it)
                selectedMovieForSave = null
            },
            onSaveToCollection = { movie, colName ->
                onSaveToCollection(movie, colName)
                selectedMovieForSave = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveOptionsBottomSheet(
    movie: DataMovie,
    collections: List<DataCollection>,
    onDismiss: () -> Unit,
    onSaveLocal: (DataMovie) -> Unit,
    onSaveToAccount: (DataMovie) -> Unit,
    onSaveToCollection: (DataMovie, String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1A1A1A),
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = "Lưu phim: ${movie.NameMovie}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Option 1: Quick Save (Local)
            ListItem(
                headlineContent = { Text("Lưu nhanh vào máy (Offline)") },
                leadingContent = { Icon(Icons.Default.DownloadForOffline, contentDescription = null, tint = Color(0xFF00E5FF)) },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                modifier = Modifier.clickable { onSaveLocal(movie) }
            )

            // Option 2: Save to Account (Cloud Sync)
            ListItem(
                headlineContent = { Text("Lưu vào tài khoản (Đồng bộ Cloud)") },
                leadingContent = { Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Color(0xFF4CAF50)) },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                modifier = Modifier.clickable { onSaveToAccount(movie) }
            )

            HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp)

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Lưu vào bộ sưu tập (Cloud)", fontSize = 14.sp, color = Color.Gray)

            if (collections.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text("Bạn chưa có bộ sưu tập nào.\nHãy tạo trong mục Profile.", textAlign = TextAlign.Center, color = Color.Gray, fontSize = 12.sp)
                }
            } else {
                collections.forEach { collection ->
                    ListItem(
                        headlineContent = { Text(collection.CollectionName) },
                        leadingContent = { Icon(Icons.Default.Folder, contentDescription = null, tint = Color.Yellow) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier.clickable { onSaveToCollection(movie, collection.CollectionName) }
                    )
                }
            }
        }
    }
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
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Person, "Profile", tint = MaterialTheme.colorScheme.primary)
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
                item { 
                    MovieSection(
                        title = "🎬 Danh sách phim", 
                        movies = uiState.movieList, 
                        savedMovieIds = uiState.savedMovieIds,
                        onSaveClick = onSaveClick, 
                        onMovieClick = onMovieClick
                    ) 
                }
                item { 
                    MovieSection(
                        title = "🎬 Phim hot", 
                        movies = uiState.movieList, 
                        savedMovieIds = uiState.savedMovieIds,
                        onSaveClick = onSaveClick, 
                        onMovieClick = onMovieClick
                    ) 
                }
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
fun MovieSection(
    title: String, 
    movies: List<DataMovie>, 
    savedMovieIds: Set<String>,
    onSaveClick: (DataMovie) -> Unit, 
    onMovieClick: (String) -> Unit
) {
    Column(Modifier.padding(top = 20.dp)) {
        Text(title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 16.dp, bottom = 12.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(movies) { movie -> 
                MovieCard(
                    movie = movie, 
                    isSaved = savedMovieIds.contains(movie.IDMovie),
                    onSaveClick = { onSaveClick(movie) }, 
                    onMovieClick = { onMovieClick(movie.IDMovie) }
                ) 
            }
        }
    }
}

@Composable
fun MovieCard(movie: DataMovie, isSaved: Boolean, onSaveClick: () -> Unit, onMovieClick: () -> Unit) {
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
                    Icon(
                        imageVector = if (isSaved) Icons.Default.BookmarkAdded else Icons.Default.BookmarkBorder, 
                        contentDescription = "Save", 
                        tint = if (isSaved) Color(0xFFFFD700) else Color.White, 
                        modifier = Modifier.size(18.dp)
                    )
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
                            IDMovie = "1",
                            NameMovie = "Avengers: Endgame",
                            Description = "The remaining Avengers must gather their allies and take a stand.",
                            URLimage = "https://example.com/endgame.jpg"
                        ),
                        DataMovie(
                            IDMovie = "2",
                            NameMovie = "Inception",
                            Description = "A thief who steals corporate secrets through the use of dream-sharing technology.",
                            URLimage = "https://example.com/inception.jpg"
                        )
                    ),
                    savedMovieIds = setOf("1"),
                    isLoading = false
                ),
                onLogout = {},
                onProfileClick = {},
                onMovieClick = {},
                onSettingsClick = {},
                onSearchClick = {},
                onSaveLocal = {},
                onSaveToAccount = {},
                onSaveToCollection = { _, _ -> },
                onFetchCollections = {},
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
            savedMovieIds = emptySet(),
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
            isSaved = true,
            onSaveClick = {},
            onMovieClick = {}
        )
    }
}
