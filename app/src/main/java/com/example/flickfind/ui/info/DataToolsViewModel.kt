package com.example.flickfind.ui.info

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.DATALAYER.Room.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

data class DataToolsUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val exportPath: String = ""
)

class DataToolsViewModel(private val app: Application) : AndroidViewModel(app) {

    private val repository = Repository(
        remote = AppRemote(),
        movieDao = AppDatabase.getDatabase(app).movieDao()
    )

    private val _uiState = MutableStateFlow(DataToolsUiState())
    val uiState = _uiState.asStateFlow()

    private val exportFile: File
        get() = File(app.getExternalFilesDir(null) ?: app.filesDir, "flickfind_saved_movies.json")

    fun exportSavedMovies() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, message = null)
            runCatching {
                val movies = repository.getAllSavedMoviesFlow().first()
                val json = JSONArray()
                movies.forEach { movie -> json.put(movie.toJson()) }
                exportFile.writeText(json.toString(2))
                _uiState.value = DataToolsUiState(
                    isLoading = false,
                    message = "Đã xuất ${movies.size} phim ra JSON",
                    exportPath = exportFile.absolutePath
                )
            }.onFailure { error ->
                _uiState.value = DataToolsUiState(
                    isLoading = false,
                    message = "Lỗi xuất JSON: ${error.message.orEmpty()}",
                    exportPath = exportFile.absolutePath
                )
            }
        }
    }

    fun importSavedMovies() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, message = null)
            runCatching {
                if (!exportFile.exists()) error("Chưa có file JSON để nhập")
                val array = JSONArray(exportFile.readText())
                repeat(array.length()) { index ->
                    repository.saveMovieToLocal(array.getJSONObject(index).toMovie())
                }
                _uiState.value = DataToolsUiState(
                    isLoading = false,
                    message = "Đã nhập ${array.length()} phim từ JSON",
                    exportPath = exportFile.absolutePath
                )
            }.onFailure { error ->
                _uiState.value = DataToolsUiState(
                    isLoading = false,
                    message = "Lỗi nhập JSON: ${error.message.orEmpty()}",
                    exportPath = exportFile.absolutePath
                )
            }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }

    private fun DataMovie.toJson(): JSONObject {
        return JSONObject()
            .put("IDMovie", IDMovie)
            .put("NameMovie", NameMovie)
            .put("Description", Description)
            .put("IDStudio", IDStudio)
            .put("URLimage", URLimage)
            .put("TimeOneEP", TimeOneEP)
            .put("NummberEP", NummberEP)
            .put("Category", Category)
            .put("Studio", Studio)
            .put("Year", Year)
    }

    private fun JSONObject.toMovie(): DataMovie {
        return DataMovie(
            IDMovie = optString("IDMovie"),
            NameMovie = optString("NameMovie"),
            Description = optString("Description"),
            IDStudio = optString("IDStudio"),
            URLimage = optString("URLimage"),
            TimeOneEP = optString("TimeOneEP"),
            NummberEP = optString("NummberEP"),
            Category = optString("Category"),
            Studio = optString("Studio"),
            Year = optString("Year")
        )
    }
}
