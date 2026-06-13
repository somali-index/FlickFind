package com.example.flickfind.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.ui.common.LoadingDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movieId: String,
    onBackClick: () -> Unit,
    onUnderDevelopmentClick: () -> Unit,
    viewModel: MovieDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showSaveOptions by remember { mutableStateOf(false) }

    LaunchedEffect(movieId) {
        viewModel.getMovieById(movieId)
    }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F0F0F)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF00E5FF))
        }
        return
    }

    val movie = uiState.movie

    if (movie == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F0F0F)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Không tìm thấy bộ phim này!", color = Color.White)
        }
        return
    }

    Scaffold(
        containerColor = Color(0xFF0F0F0F),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                title = { Text(text = "Chi tiết phim", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onUnderDevelopmentClick) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                    IconButton(onClick = { 
                        viewModel.fetchUserCollections()
                        showSaveOptions = true 
                    }) {
                        Icon(
                            imageVector = if (uiState.isSaved) Icons.Default.BookmarkAdded else Icons.Default.BookmarkBorder,
                            contentDescription = "Save",
                            tint = if (uiState.isSaved) Color(0xFFFFD700) else Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Ảnh phim lớn với Gradient overlay
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)) {
                AsyncImage(
                    model = movie.URLimage,
                    contentDescription = movie.NameMovie,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0xFF0F0F0F)),
                                startY = 300f
                            )
                        )
                )
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                // 2. Tên phim
                Text(
                    text = movie.NameMovie,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    lineHeight = 34.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Tags / Info Badges
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoBadge(text = movie.Year)
                    InfoBadge(text = movie.Category)
                    InfoBadge(text = movie.NummberEP + " Tập", containerColor = Color(0xFF00E5FF).copy(alpha = 0.2f), contentColor = Color(0xFF00E5FF))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 3. Thông số chi tiết
                Text(text = "THÔNG TIN CHI TIẾT", color = Color(0xFF00E5FF), fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(12.dp))
                
                DetailRow(label = "Thời lượng", value = movie.TimeOneEP)
                DetailRow(label = "Studio", value = movie.Studio)
                DetailRow(label = "ID Phim", value = movie.IDMovie)

                Spacer(modifier = Modifier.height(24.dp))

                // 4. Nội dung
                Text(text = "NỘI DUNG", color = Color(0xFF00E5FF), fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = movie.Description.ifEmpty { "Chưa có nội dung cho bộ phim này." },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray,
                    textAlign = TextAlign.Justify,
                    lineHeight = 22.sp
                )
                
                Spacer(modifier = Modifier.height(40.dp))

                // Nút xem phim giả định
                Button(
                    onClick = onUnderDevelopmentClick,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("XEM LỊCH CHIẾU", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    if (showSaveOptions) {
        SaveOptionsBottomSheet(
            movie = movie,
            collections = uiState.collections,
            onDismiss = { showSaveOptions = false },
            onSaveLocal = {
                viewModel.saveMovie(it)
                showSaveOptions = false
            },
            onSaveToAccount = {
                viewModel.saveMovieToAccount(it)
                showSaveOptions = false
            },
            onSaveToCollection = { m, col ->
                viewModel.saveMovieToCollection(m, col)
                showSaveOptions = false
            }
        )
    }

    LoadingDialog(
        visible = uiState.isSlowLoading,
        message = "Thao tác đang mất nhiều thời gian hơn bình thường..."
    )
}

@Composable
fun InfoBadge(
    text: String,
    containerColor: Color = Color(0xFF1A1A1A),
    contentColor: Color = Color.Gray
) {
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value.ifEmpty { "N/A" }, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}
