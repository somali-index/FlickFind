package com.example.flickfind.ui.info

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.DATALAYER.Room.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MovieStatisticsUiState(
    val isLoading: Boolean = true,
    val totalMovies: Int = 0,
    val averageMinutes: Double = 0.0,
    val minDurationMovie: String = "N/A",
    val maxDurationMovie: String = "N/A",
    val byGenre: Map<String, Int> = emptyMap(),
    val byStudio: Map<String, Int> = emptyMap(),
    val errorMessage: String? = null
)

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository(
        remote = AppRemote(),
        movieDao = AppDatabase.getDatabase(application).movieDao()
    )

    private val _uiState = MutableStateFlow(MovieStatisticsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    fun loadStatistics() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        repository.getMovies { movies ->
            if (movies.isEmpty()) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Chưa có dữ liệu phim để thống kê")
                }
                return@getMovies
            }

            val durations = movies.mapNotNull { movie ->
                parseMinutes(movie.TimeOneEP)?.let { minutes -> movie to minutes }
            }
            val average = durations.map { it.second }.average().takeIf { !it.isNaN() } ?: 0.0
            val minMovie = durations.minByOrNull { it.second }?.first
            val maxMovie = durations.maxByOrNull { it.second }?.first

            _uiState.update {
                MovieStatisticsUiState(
                    isLoading = false,
                    totalMovies = movies.size,
                    averageMinutes = average,
                    minDurationMovie = minMovie?.let { "${it.NameMovie} (${it.TimeOneEP})" } ?: "N/A",
                    maxDurationMovie = maxMovie?.let { "${it.NameMovie} (${it.TimeOneEP})" } ?: "N/A",
                    byGenre = countByGroup(movies) { it.Category },
                    byStudio = countByGroup(movies) { it.Studio }
                )
            }
        }
    }

    private fun parseMinutes(value: String): Int? {
        return Regex("\\d+").find(value)?.value?.toIntOrNull()
    }

    private fun countByGroup(movies: List<DataMovie>, selector: (DataMovie) -> String): Map<String, Int> {
        return movies
            .flatMap { movie ->
                selector(movie)
                    .split(",")
                    .map { it.trim() }
                    .filter { it.isNotBlank() }
            }
            .groupingBy { it }
            .eachCount()
            .toList()
            .sortedByDescending { it.second }
            .toMap()
    }
}
