package com.example.flickfind.testTinhNang.dataLayerTest.Repository

import android.util.Log
import androidx.room.Dao
import com.example.flickfind.testTinhNang.dataLayerTest.DAO.DAOTest
import com.example.flickfind.testTinhNang.dataLayerTest.ROOM.UNITYTest
import com.example.flickfind.testTinhNang.dataLayerTest.dataClassTest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
class RepositoryTest(
    private val dao: DAOTest
) {

    private val db = FirebaseFirestore.getInstance()

    fun getMovies(
        onResult: (List<dataClassTest>) -> Unit
    ) {
        db.collection("MovieData")
            .get()
            .addOnSuccessListener { result ->

                val movieList = mutableListOf<dataClassTest>()

                for (document in result) {
                    val movie = document.toObject(dataClassTest::class.java)
                    movieList.add(movie)
                }

                onResult(movieList)
            }
    }

    fun saveMovie(movie: dataClassTest) {

        val entity = UNITYTest(
            IDMovie = movie.IDMovie,
            NameMovie = movie.NameMovie,
            Description = movie.Description,
            IDStudio = movie.IDStudio,
            URLimage = movie.URLimage
        )

        GlobalScope.launch {
            dao.insertMovie(entity)
        }
    }

    suspend fun getSavedMovies(): List<UNITYTest> {
        return dao.getAllMovies()
    }
}