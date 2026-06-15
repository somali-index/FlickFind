package com.flickfind.app.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flickfind.app.data.model.*
import com.flickfind.app.data.repository.MovieRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DetailUiData(
    val movie: Movie,
    val cast: List<CastMember> = emptyList(),
    val trailerKey: String? = null,
    val similar: List<Movie> = emptyList()
)

class MovieDetailViewModel : ViewModel() {
    private val repo = MovieRepository()

    private val _state = MutableStateFlow<UiState<DetailUiData>>(UiState.Loading)
    val state: StateFlow<UiState<DetailUiData>> = _state

    fun load(movieId: Int) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                val movieD = async { repo.getMovieDetail(movieId) }
                val creditsD = async { repo.getMovieCredits(movieId) }
                val videosD = async { repo.getMovieVideos(movieId) }
                val similarD = async { repo.getSimilarMovies(movieId) }

                val movie = movieD.await().getOrThrow()
                val credits = creditsD.await().getOrDefault(CreditsResponse())
                val videos = videosD.await().getOrDefault(VideoResponse())
                val similar = similarD.await().getOrDefault(emptyList())

                val trailerKey = videos.results.firstOrNull { it.isYouTubeTrailer }?.key

                _state.value = UiState.Success(
                    DetailUiData(
                        movie = movie,
                        cast = credits.cast.take(15),
                        trailerKey = trailerKey,
                        similar = similar.take(10)
                    )
                )
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Không thể tải thông tin phim")
            }
        }
    }
}
