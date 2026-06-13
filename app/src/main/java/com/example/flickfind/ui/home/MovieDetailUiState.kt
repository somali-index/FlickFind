package com.example.flickfind.ui.home

import com.example.flickfind.DATALAYER.DataClass.DataCollection
import com.example.flickfind.DATALAYER.DataClass.DataMovie

data class MovieDetailUiState(
    val movie: DataMovie? = null,
    val isLoading: Boolean = true,
    val isSaved: Boolean = false,
    val savedMovieIds: Set<String> = emptySet(),
    val collections: List<DataCollection> = emptyList(),
    val userMessage: String? = null
)
