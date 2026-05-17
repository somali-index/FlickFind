package com.example.flickfind.DATALAYER.AppRepository

import com.example.flickfind.DATALAYER.DAO.DAOMovie
import com.example.flickfind.DATALAYER.DataClass.DataMovieClass
import com.example.flickfind.DATALAYER.Remote.AppRemote
import kotlin.jvm.java

class Repository(private val dao: DAOMovie,private val remote: AppRemote) {
    val db = remote.creatRemoteFS()

    fun getMovies(
        onResult: (List<DataMovieClass>) -> Unit
    ) {
        db.collection("MovieData")
            .get()
            .addOnSuccessListener { result ->

                val movieList = mutableListOf<DataMovieClass>()

                for (document in result) {
                    val movie = document.toObject(DataMovieClass::class.java)
                    movieList.add(movie)
                }

                onResult(movieList)
            }
    }


}