package com.example.flickfind.ui.SearchUI

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.ui.home.SaveOptionsBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieSearchScreen(
    modifier: Modifier = Modifier,
    viewModel: MovieSearchViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedMovieForSave by remember { mutableStateOf<DataMovie?>(null) }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.clearMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // 1. Ô NHẬP TÌM KIẾM PHIM
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { chuMoiGoc ->
                    viewModel.onSearchQueryChange(chuMoiGoc)
                },
                label = { Text("Nhập tên phim cần tìm (ví dụ: Conan, Naruto)...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. DANH SÁCH CÁC BỘ PHIM TÌM ĐƯỢC
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(uiState.movieList) { phim ->
                    val isSaved = uiState.savedMovieIds.contains(phim.IDMovie)
                    SearchMovieCard(
                        movie = phim,
                        isSaved = isSaved,
                        onSaveClick = {
                            selectedMovieForSave = phim
                            viewModel.fetchUserCollections()
                        }
                    )
                }
            }

            // 3. THÔNG BÁO NẾU KHÔNG TÌM THẤY PHIM
            if (uiState.movieList.isEmpty() && uiState.searchQuery.isNotBlank()) {
                Text(
                    text = "Không tìm thấy bộ phim nào phù hợp mục tiêu @@",
                    modifier = Modifier.padding(top = 16.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    if (selectedMovieForSave != null) {
        SaveOptionsBottomSheet(
            movie = selectedMovieForSave!!,
            collections = uiState.collections,
            onDismiss = { selectedMovieForSave = null },
            onSaveLocal = {
                viewModel.saveMovie(it)
                selectedMovieForSave = null
            },
            onSaveToAccount = {
                viewModel.saveMovieToAccount(it)
                selectedMovieForSave = null
            },
            onSaveToCollection = { movie, colName ->
                viewModel.saveMovieToCollection(movie, colName)
                selectedMovieForSave = null
            }
        )
    }
}

@Composable
fun SearchMovieCard(
    movie: DataMovie,
    isSaved: Boolean,
    onSaveClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ảnh phim
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(width = 80.dp, height = 110.dp)
            ) {
                AsyncImage(
                    model = movie.URLimage,
                    contentDescription = movie.NameMovie,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Thông tin phim
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movie.NameMovie,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Thời lượng: ${movie.TimeOneEP}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = "Số tập: ${movie.NummberEP}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movie.Year,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Nút lưu
            IconButton(
                onClick = onSaveClick,
                modifier = Modifier
                    .background(Color.Black.copy(0.3f), CircleShape)
                    .size(36.dp)
            ) {
                Icon(
                    imageVector = if (isSaved) Icons.Default.BookmarkAdded else Icons.Default.BookmarkBorder,
                    contentDescription = "Save",
                    tint = if (isSaved) Color(0xFFFFD700) else Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
