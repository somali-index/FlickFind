package com.example.flickfind.testTinhNang.UITest

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickfind.testTinhNang.dataLayerTest.Repository.RepositoryTest
import com.example.flickfind.testTinhNang.dataLayerTest.dataClassTest
import coil.compose.AsyncImage
import com.example.flickfind.testTinhNang.dataLayerTest.AppDatabase
import com.example.flickfind.testTinhNang.dataLayerTest.ROOM.UNITYTest
import kotlinx.coroutines.launch


class ViewModelTest(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val dao = db.movieDao()

    private val repository = RepositoryTest(dao)

    var savedMovies = mutableStateListOf<UNITYTest>()
    var movieList by mutableStateOf<List<dataClassTest>>(emptyList())

    fun loadMovies() {
        repository.getMovies { movies ->
            movieList = movies
        }
    }

    fun loadSavedMovies() {
        viewModelScope.launch {
            savedMovies.clear()
            savedMovies.addAll(repository.getSavedMovies())
        }
    }

    fun saveMovie(movie: dataClassTest) {
        repository.saveMovie(movie)

        viewModelScope.launch {
            loadSavedMovies()
        }
    }
}