package com.example.flickfind.ui.home

import androidx.lifecycle.ViewModel
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.DATALAYER.Remote.AppRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeUiState(

    val movieList: List<DataMovie> = emptyList(),

    val isLoading: Boolean = true
)

class HomeViewModel : ViewModel() {

    private val repository = Repository(
        remote = AppRemote()
    )

    // STATE
    private val _homeUiState =
        MutableStateFlow(HomeUiState())

    val homeUiState =
        _homeUiState.asStateFlow()

    // CACHE
    private var allMoviesHome:
            List<DataMovie> = emptyList()

    init {

        getMovieListHome()
    }

    private fun getMovieListHome() {

        repository.getMovies { movieList ->

            allMoviesHome = movieList

            _homeUiState.value =
                HomeUiState(

                    movieList = movieList,

                    isLoading = false
                )
        }
    }
}