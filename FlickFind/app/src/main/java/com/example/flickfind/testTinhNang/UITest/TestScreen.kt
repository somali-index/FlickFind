package com.example.flickfind.testTinhNang.UITest

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button


@Composable
fun MovieScreen(
    viewModel: ViewModelTest = viewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.loadMovies()
    }

    LazyColumn {

        items(viewModel.movieList) { movie ->

            Text(text = movie.URLimage)

            AsyncImage(
                model = movie.URLimage,
                contentDescription = movie.NameMovie,
                modifier = Modifier.size(150.dp)
            )

            Text(text = movie.NameMovie)

            Text(text = movie.Description)
            Button(
                onClick = {
                    viewModel.saveMovie(movie)
                }
            ) {
                Text("Lưu")
            }
        }
    }
}