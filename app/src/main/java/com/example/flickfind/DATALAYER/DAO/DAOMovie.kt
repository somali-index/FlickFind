package com.example.flickfind.DATALAYER.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flickfind.DATALAYER.Room.RoomMovies

@Dao
interface DAOMovie {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: RoomMovies)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(list: List<RoomMovies>)

    @Query("SELECT * FROM movieData")
    suspend fun getAllMovies(): List<RoomMovies>

    @Query("DELETE FROM movieData")
    suspend fun clearAll()


}
