package com.example.flickfind.ui.SearchUI

import com.example.flickfind.DATALAYER.DataClass.DataCollection
import com.example.flickfind.DATALAYER.DataClass.DataMovie

data class MovieSearchUiState(
    val searchQuery: String = "",
    val movieList: List<DataMovie> = emptyList(),
    val savedMovieIds: Set<String> = emptySet(),
    val collections: List<DataCollection> = emptyList(),
    val userMessage: String? = null,
    val isLoading: Boolean = false,
    val isSlowLoading: Boolean = false
)
