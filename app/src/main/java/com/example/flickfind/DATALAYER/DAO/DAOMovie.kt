package com.example.flickfind.DATALAYER.DAO

import androidx.room.*
import com.example.flickfind.DATALAYER.Room.*


data class MovieWithGenres(
    @Embedded val movie: RoomMovies,
    @Relation(
        parentColumn = "IDMovie",
        entityColumn = "GenreID",
        associateBy = Junction(MovieGenreCrossRef::class)
    )
    val genres: List<RoomGenre>
)


data class MovieWithStudios(
    @Embedded val movie: RoomMovies,
    @Relation(
        parentColumn = "IDMovie",
        entityColumn = "IDStudio",
        associateBy = Junction(MovieStudioCrossRef::class)
    )
    val studios: List<RoomStudio>
)

@Dao
interface DAOMovie {

    // CÁC HÀM CHO PHIM
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: RoomMovies)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(list: List<RoomMovies>)

    @Query("SELECT * FROM movieData")
    suspend fun getAllMovies(): List<RoomMovies>

    @Delete
    suspend fun deleteMovie(movie: RoomMovies)

    // CÁC HÀM CHO THỂ LOẠI va STUDIO
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<RoomGenre>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudios(studios: List<RoomStudio>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieGenreCrossRef(crossRef: MovieGenreCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieStudioCrossRef(crossRef: MovieStudioCrossRef)

    //  TRUY VẤN QUAN HỆ (Nối bảng)
    @Transaction
    @Query("SELECT * FROM movieData")
    suspend fun getMoviesWithGenres(): List<MovieWithGenres>

    @Transaction
    @Query("SELECT * FROM movieData WHERE IDMovie = :movieId")
    suspend fun getMovieWithGenresById(movieId: String): MovieWithGenres?

    @Transaction
    @Query("SELECT * FROM movieData")
    suspend fun getMoviesWithStudios(): List<MovieWithStudios>

    //  XÓA DL
    @Query("DELETE FROM movieData")
    suspend fun clearAll()
}
