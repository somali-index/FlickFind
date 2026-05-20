package com.example.flickfind.ui.home

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
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
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.DATALAYER.DataClass.ListMovieDataSource
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.ui.SearchUI.SearchActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScree(
    onLogout: () -> Unit,

    viewModel: HomeViewModel = viewModel()
) {

    val context = LocalContext.current

    // LẤY STATE TỪ VIEWMODEL
    val uiState by
    viewModel.homeUiState.collectAsState()

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.Movie,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text(
                            text = "FlickFind",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },

                actions = {

                    // SEARCH
                    IconButton(

                        onClick = {

                            context.startActivity(

                                Intent(
                                    context,
                                    SearchActivity::class.java
                                )
                            )
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }

                    // LOGOUT
                    TextButton(
                        onClick = onLogout
                    ) {

                        Text("Đăng xuất")
                    }
                }
            )
        }

    ) { paddingValues ->

        // LOADING
        if (uiState.isLoading) {

            Box(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),

                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator()
            }

        } else {

            // DATA
            LazyColumn(

                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0F0F0F))
                    .padding(paddingValues),

                contentPadding =
                    PaddingValues(bottom = 24.dp)

            ) {

                item {

                    HomeHeader()
                }

                item {

                    MovieSection(
                        title = "🎬 Danh sách phim",
                        movies = uiState.movieList
                    )
                }
                item {

                    MovieSection(
                        title = "🎬 Phim hot",
                        movies = uiState.movieList
                    )
                }
            }
        }
    }
}
@Composable
fun HomeHeader() {

    Column(

        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 48.dp,
                bottom = 8.dp
            )
    ) {

        Text(
            text = "Khám phá thế giới phim cùng",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Text(
            text = "FlickFind",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MovieSection(
    title: String,
    movies: List<DataMovie>
) {

    Column(
        modifier = Modifier.padding(top = 20.dp)
    ) {

        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(
                start = 16.dp,
                bottom = 12.dp
            )
        )

        LazyRow(

            contentPadding =
                PaddingValues(horizontal = 16.dp),

            horizontalArrangement =
                Arrangement.spacedBy(12.dp)

        ) {

            items(movies) { movie ->

                MovieCard(movie)
            }
        }
    }
}

@Composable
fun MovieCard(
    movie: DataMovie
) {

    Column(
        modifier = Modifier.width(120.dp)
    ) {

        Card(

            shape = RoundedCornerShape(10.dp),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),

            modifier = Modifier
                .width(120.dp)
                .height(180.dp)

        ) {

            AsyncImage(

                model = movie.URLimage,

                contentDescription = movie.NameMovie,

                contentScale = ContentScale.Crop,

                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = movie.NameMovie,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = movie.Description,
            color = Color.Gray,
            fontSize = 11.sp,
            maxLines = 1
        )
    }
}