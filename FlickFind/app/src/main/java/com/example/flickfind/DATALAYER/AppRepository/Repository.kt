package com.example.flickfind.DATALAYER.AppRepository

import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.testTinhNang.dataLayerTest.DAO.DAOTest
import com.example.flickfind.testTinhNang.dataLayerTest.dataClassTest

class Repository(private val dao: DAOTest,private val remote: AppRemote) {
    val db = remote.creatRemoteFS()

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


}