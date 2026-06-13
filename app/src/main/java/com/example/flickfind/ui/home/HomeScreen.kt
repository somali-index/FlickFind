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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.DATALAYER.DataClass.DataCollection
import com.example.flickfind.ui.SearchUI.SearchActivity

@Composable
fun HomeScree(
    onLogout: () -> Unit,
    onProfileClick: () -> Unit,
    onMovieClick: () -> Unit,
    onSettingsClick: () -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.homeUiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
            viewModel.clearMessage()
        }
    }

    HomeScreeContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onLogout = onLogout,
        onProfileClick = onProfileClick,
        onSettingsClick = onMovieClick,
        onSearchClick = {
            context.startActivity(Intent(context, SearchActivity::class.java))
        },
        onSaveClick = { movie -> viewModel.saveMovie(movie) },
        onSaveToCollection = { movie, id, name -> viewModel.saveMovieToCollection(movie, id, name) },
        onFetchCollections = { viewModel.fetchUserCollections() },
        onMovieClick = onMovieClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreeContent(
    uiState: HomeUiState,
    snackbarHostState: SnackbarHostState,
    onLogout: () -> Unit,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSaveClick: (DataMovie) -> Unit,
    onSaveToCollection: (DataMovie, String, String) -> Unit,
    onFetchCollections: () -> Unit,
    onMovieClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var selectedMovieForSave by remember { mutableStateOf<DataMovie?>(null) }

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
        selectedMovieForSave?.let { movie ->
            SaveMovieDialog(
                movie = movie,
                collections = uiState.collections,
                onDismiss = { selectedMovieForSave = null },
                onQuickSave = { onSaveClick(movie) },
                onSaveToCollection = { colId, colName -> onSaveToCollection(movie, colId, colName) },
                onFetchCollections = onFetchCollections
            )
        }

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
                item { MovieSection("🎬 Danh sách phim", uiState.movieList, { selectedMovieForSave = it }, onMovieClick) }
                item { MovieSection("🎬 Phim hot", uiState.movieList, { selectedMovieForSave = it }, onMovieClick) }
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
fun MovieSection(title: String, movies: List<DataMovie>, onSaveClick: (DataMovie) -> Unit, onMovieClick: () -> Unit) {
    Column(Modifier.padding(top = 20.dp)) {
        Text(title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 16.dp, bottom = 12.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(movies) { movie -> MovieCard(movie, { onSaveClick(movie) }, onMovieClick) }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveMovieDialog(
    movie: DataMovie,
    collections: List<DataCollection>,
    onDismiss: () -> Unit,
    onQuickSave: () -> Unit,
    onSaveToCollection: (String, String) -> Unit,
    onFetchCollections: () -> Unit
) {
    var showCollectionList by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1A1A1A),
        title = {
            Text(
                text = if (showCollectionList) "Chọn bộ sưu tập" else "Lưu phim",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (!showCollectionList) {
                    ListItem(
                        headlineContent = { Text("Lưu nhanh", color = Color.White) },
                        supportingContent = { Text("Lưu vào danh sách mặc định", color = Color.Gray) },
                        leadingContent = { Icon(Icons.Default.Bookmark, null, tint = Color(0xFF00E5FF)) },
                        modifier = Modifier.clickable { onQuickSave(); onDismiss() },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                    ListItem(
                        headlineContent = { Text("Lưu vào bộ sưu tập", color = Color.White) },
                        supportingContent = { Text("Chọn một bộ sưu tập cụ thể", color = Color.Gray) },
                        leadingContent = { Icon(Icons.Default.Folder, null, tint = Color(0xFF00E5FF)) },
                        modifier = Modifier.clickable { onFetchCollections(); showCollectionList = true },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                } else {
                    if (collections.isEmpty()) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
                            Text("Chưa có bộ sưu tập nào", color = Color.Gray)
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = { /* Chức năng tạo mới chưa làm */ }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF))) {
                                Text("Tạo mới", color = Color.Black)
                            }
                        }
                    } else {
                        LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                            items(collections) { collection ->
                                ListItem(
                                    headlineContent = { Text(collection.CollectionName, color = Color.White) },
                                    leadingContent = { Icon(Icons.Default.Folder, null, tint = Color(0xFF00E5FF)) },
                                    modifier = Modifier.clickable { onSaveToCollection(collection.IDCollection, collection.CollectionName); onDismiss() },
                                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                                )
                            }
                            item {
                                TextButton(onClick = { /* Chức năng tạo mới chưa làm */ }, modifier = Modifier.fillMaxWidth()) {
                                    Icon(Icons.Default.Add, null, tint = Color(0xFF00E5FF))
                                    Spacer(Modifier.width(8.dp))
                                    Text("Tạo bộ sưu tập mới", color = Color(0xFF00E5FF))
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (showCollectionList) {
                TextButton(onClick = { showCollectionList = false }) { Text("Quay lại", color = Color(0xFF00E5FF)) }
            } else {
                TextButton(onClick = onDismiss) { Text("Hủy", color = Color.Gray) }
            }
        }
    )
}
