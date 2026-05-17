package com.example.movieapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movieapp.data.MovieDataSource
import com.example.movieapp.model.Movie

@Composable
fun HomeScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F0F)),  // nền tối
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Header
        item {
            HomeHeader()
        }


        item {
            MovieSection(
                title = "🎲 Ngẫu Nhiên",
                movies = MovieDataSource.randomMovies
            )
        }


        item {
            MovieSection(
                title = "🔥 Top Hot",
                movies = MovieDataSource.hotMovies
            )
        }


        item {
            MovieSection(
                title = "🎬 Sắp Chiếu",
                movies = MovieDataSource.comingSoonMovies
            )
        }


        item {
            MovieSection(
                title = "Đã Full",
                movies = MovieDataSource.completedMovies
            )
        }
    }
}

// ─────────────────────────────────────────────
// HEADER
// ─────────────────────────────────────────────
@Composable
fun HomeHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 8.dp)
    ) {
        Text(
            text = "Khám phá thế giới phim cùng",
            color = Color(0xFF9E9E9E),
            fontSize = 14.sp
        )
        Text(
            text = "Phê Phim",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// ─────────────────────────────────────────────
//  (tiêu đề + LazyRow)
// ─────────────────────────────────────────────
@Composable
fun MovieSection(
    title: String,
    movies: List<Movie>
) {
    Column(modifier = Modifier.padding(top = 20.dp)) {

        // Tiêu đề
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 16.dp, bottom = 12.dp)
        )

        // Danh sách cuộn ngang
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(movies) { movie ->
                MovieCard(movie = movie)
            }
        }
    }
}

// ─────────────────────────────────────────────
// CARD TỪNG PHIM
// ─────────────────────────────────────────────
@Composable
fun MovieCard(movie: Movie) {
    Column(
        modifier = Modifier.width(120.dp)
    ) {
        // Poster dọc
        Card(
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .width(120.dp)
                .height(180.dp)
        ) {
            Image(
                painter = painterResource(id = movie.imageResId),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Tên phim
        Text(
            text = movie.title,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // Thể loại
        Text(
            text = movie.genre,
            color = Color(0xFF9E9E9E),
            fontSize = 11.sp,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F0F)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}