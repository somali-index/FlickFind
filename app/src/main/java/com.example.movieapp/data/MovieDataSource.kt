package com.example.movieapp.data

import com.example.movieapp.model.Movie
import com.example.flickfind.R

object MovieDataSource {

    val randomMovies = listOf(
        Movie(1, "Dune: Part Two", "Sci-Fi", R.drawable.ic_launcher_foreground),
        Movie(2, "Oppenheimer", "Drama", R.drawable.ic_launcher_foreground),
        Movie(3, "The Batman", "Action", R.drawable.ic_launcher_foreground),
        Movie(4, "Avatar 2", "Sci-Fi", R.drawable.ic_launcher_foreground),
        Movie(5, "Interstellar", "Sci-Fi", R.drawable.ic_launcher_foreground),
    )

    val hotMovies = listOf(
        Movie(6, "Avengers: Endgame", "Action", R.drawable.ic_launcher_foreground),
        Movie(7, "Spider-Man NWH", "Action", R.drawable.ic_launcher_foreground),
        Movie(8, "Top Gun: Maverick", "Action", R.drawable.ic_launcher_foreground),
        Movie(9, "Black Panther", "Action", R.drawable.ic_launcher_foreground),
        Movie(10, "Doctor Strange", "Fantasy", R.drawable.ic_launcher_foreground),
    )

    val comingSoonMovies = listOf(
        Movie(11, "Deadpool & Wolverine", "Action", R.drawable.ic_launcher_foreground),
        Movie(12, "Inside Out 2", "Animation", R.drawable.ic_launcher_foreground),
        Movie(13, "Furiosa", "Action", R.drawable.ic_launcher_foreground),
        Movie(14, "Alien: Romulus", "Horror", R.drawable.ic_launcher_foreground),
        Movie(15, "Twisters", "Action", R.drawable.ic_launcher_foreground),
    )

    val completedMovies = listOf(
        Movie(16, "Breaking Bad", "Crime", R.drawable.ic_launcher_foreground),
        Movie(17, "Game of Thrones", "Fantasy", R.drawable.ic_launcher_foreground),
        Movie(18, "The Office", "Comedy", R.drawable.ic_launcher_foreground),
        Movie(19, "Stranger Things", "Horror", R.drawable.ic_launcher_foreground),
        Movie(20, "Dark", "Sci-Fi", R.drawable.ic_launcher_foreground),
    )
}
