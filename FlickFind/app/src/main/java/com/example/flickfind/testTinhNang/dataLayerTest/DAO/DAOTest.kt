package com.example.flickfind.testTinhNang.dataLayerTest.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flickfind.testTinhNang.dataLayerTest.ROOM.UNITYTest

@Dao
interface DAOTest {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: UNITYTest)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(list: List<UNITYTest>)

    @Query("SELECT * FROM movieData")
    suspend fun getAllMovies(): List<UNITYTest>

    @Query("DELETE FROM movieData")
    suspend fun clearAll()


}