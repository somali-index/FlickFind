package com.example.flickfind.DATALAYER.AppRepository

import com.example.flickfind.DATALAYER.DataClass.DataMovie
import com.example.flickfind.DATALAYER.Remote.AppRemote
import kotlin.jvm.java

class Repository(private val remote: AppRemote) {
    val db = remote.creatRemoteFS()


    fun getMovies(
        onResult: (List<DataMovie>) -> Unit
    ) {
        db.collection("MovieData")
            .get()
            .addOnSuccessListener { result ->

                val movieList = mutableListOf<DataMovie>()

                for (document in result) {
                    val movie = document.toObject(DataMovie::class.java)
                    movieList.add(movie)
                }

                onResult(movieList)
            }
    }


}