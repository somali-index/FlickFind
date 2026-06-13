package com.example.flickfind.DATALAYER.DAO

import androidx.collection.ArrayMap
import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.appendPlaceholders
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.room.util.recursiveFetchArrayMap
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.SQLiteStatement
import com.example.flickfind.DATALAYER.Room.MovieGenreCrossRef
import com.example.flickfind.DATALAYER.Room.MovieStudioCrossRef
import com.example.flickfind.DATALAYER.Room.RoomGenre
import com.example.flickfind.DATALAYER.Room.RoomMovies
import com.example.flickfind.DATALAYER.Room.RoomStudio
import com.example.flickfind.DATALAYER.Room.RoomUser
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlin.text.StringBuilder
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class DAOMovie_Impl(
  __db: RoomDatabase,
) : DAOMovie {
  private val __db: RoomDatabase

  private val __insertAdapterOfRoomMovies: EntityInsertAdapter<RoomMovies>

  private val __insertAdapterOfRoomGenre: EntityInsertAdapter<RoomGenre>

  private val __insertAdapterOfRoomStudio: EntityInsertAdapter<RoomStudio>

  private val __insertAdapterOfMovieGenreCrossRef: EntityInsertAdapter<MovieGenreCrossRef>

  private val __insertAdapterOfMovieStudioCrossRef: EntityInsertAdapter<MovieStudioCrossRef>

  private val __insertAdapterOfRoomUser: EntityInsertAdapter<RoomUser>

  private val __deleteAdapterOfRoomMovies: EntityDeleteOrUpdateAdapter<RoomMovies>
  init {
    this.__db = __db
    this.__insertAdapterOfRoomMovies = object : EntityInsertAdapter<RoomMovies>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `movieData` (`IDMovie`,`NameMovie`,`Description`,`IDStudio`,`URLimage`,`TimeOneEP`,`NummberEP`) VALUES (?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: RoomMovies) {
        statement.bindText(1, entity.IDMovie)
        statement.bindText(2, entity.NameMovie)
        statement.bindText(3, entity.Description)
        statement.bindText(4, entity.IDStudio)
        statement.bindText(5, entity.URLimage)
        statement.bindText(6, entity.TimeOneEP)
        statement.bindText(7, entity.NummberEP)
      }
    }
    this.__insertAdapterOfRoomGenre = object : EntityInsertAdapter<RoomGenre>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `movieGenre` (`GenreID`,`GenreName`,`description`) VALUES (?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: RoomGenre) {
        statement.bindText(1, entity.GenreID)
        statement.bindText(2, entity.GenreName)
        statement.bindText(3, entity.description)
      }
    }
    this.__insertAdapterOfRoomStudio = object : EntityInsertAdapter<RoomStudio>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `studioData` (`IDStudio`,`StudioName`) VALUES (?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: RoomStudio) {
        statement.bindText(1, entity.IDStudio)
        statement.bindText(2, entity.StudioName)
      }
    }
    this.__insertAdapterOfMovieGenreCrossRef = object : EntityInsertAdapter<MovieGenreCrossRef>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `Genre_Movie` (`GenreID`,`IDMovie`) VALUES (?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: MovieGenreCrossRef) {
        statement.bindText(1, entity.GenreID)
        statement.bindText(2, entity.IDMovie)
      }
    }
    this.__insertAdapterOfMovieStudioCrossRef = object : EntityInsertAdapter<MovieStudioCrossRef>()
        {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `Studio_Movie` (`IDStudio`,`IDMovie`) VALUES (?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: MovieStudioCrossRef) {
        statement.bindText(1, entity.IDStudio)
        statement.bindText(2, entity.IDMovie)
      }
    }
    this.__insertAdapterOfRoomUser = object : EntityInsertAdapter<RoomUser>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `userData` (`IDUser`,`Email`,`Pass`,`UserName`,`avatar`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: RoomUser) {
        statement.bindText(1, entity.IDUser)
        statement.bindText(2, entity.Email)
        statement.bindText(3, entity.Pass)
        statement.bindText(4, entity.UserName)
        statement.bindText(5, entity.avatar)
      }
    }
    this.__deleteAdapterOfRoomMovies = object : EntityDeleteOrUpdateAdapter<RoomMovies>() {
      protected override fun createQuery(): String = "DELETE FROM `movieData` WHERE `IDMovie` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: RoomMovies) {
        statement.bindText(1, entity.IDMovie)
      }
    }
  }

  public override suspend fun insertMovie(movie: RoomMovies): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfRoomMovies.insert(_connection, movie)
  }

  public override suspend fun insertMovies(list: List<RoomMovies>): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfRoomMovies.insert(_connection, list)
  }

  public override suspend fun insertGenres(genres: List<RoomGenre>): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfRoomGenre.insert(_connection, genres)
  }

  public override suspend fun insertStudios(studios: List<RoomStudio>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfRoomStudio.insert(_connection, studios)
  }

  public override suspend fun insertMovieGenreCrossRef(crossRef: MovieGenreCrossRef): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfMovieGenreCrossRef.insert(_connection, crossRef)
  }

  public override suspend fun insertMovieStudioCrossRef(crossRef: MovieStudioCrossRef): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfMovieStudioCrossRef.insert(_connection, crossRef)
  }

  public override suspend fun insertUser(user: RoomUser): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfRoomUser.insert(_connection, user)
  }

  public override suspend fun deleteMovie(movie: RoomMovies): Unit = performSuspending(__db, false,
      true) { _connection ->
    __deleteAdapterOfRoomMovies.handle(_connection, movie)
  }

  public override fun getAllMoviesFlow(): Flow<List<RoomMovies>> {
    val _sql: String = "SELECT * FROM movieData"
    return createFlow(__db, false, arrayOf("movieData")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfIDMovie: Int = getColumnIndexOrThrow(_stmt, "IDMovie")
        val _cursorIndexOfNameMovie: Int = getColumnIndexOrThrow(_stmt, "NameMovie")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "Description")
        val _cursorIndexOfIDStudio: Int = getColumnIndexOrThrow(_stmt, "IDStudio")
        val _cursorIndexOfURLimage: Int = getColumnIndexOrThrow(_stmt, "URLimage")
        val _cursorIndexOfTimeOneEP: Int = getColumnIndexOrThrow(_stmt, "TimeOneEP")
        val _cursorIndexOfNummberEP: Int = getColumnIndexOrThrow(_stmt, "NummberEP")
        val _result: MutableList<RoomMovies> = mutableListOf()
        while (_stmt.step()) {
          val _item: RoomMovies
          val _tmpIDMovie: String
          _tmpIDMovie = _stmt.getText(_cursorIndexOfIDMovie)
          val _tmpNameMovie: String
          _tmpNameMovie = _stmt.getText(_cursorIndexOfNameMovie)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          val _tmpIDStudio: String
          _tmpIDStudio = _stmt.getText(_cursorIndexOfIDStudio)
          val _tmpURLimage: String
          _tmpURLimage = _stmt.getText(_cursorIndexOfURLimage)
          val _tmpTimeOneEP: String
          _tmpTimeOneEP = _stmt.getText(_cursorIndexOfTimeOneEP)
          val _tmpNummberEP: String
          _tmpNummberEP = _stmt.getText(_cursorIndexOfNummberEP)
          _item =
              RoomMovies(_tmpIDMovie,_tmpNameMovie,_tmpDescription,_tmpIDStudio,_tmpURLimage,_tmpTimeOneEP,_tmpNummberEP)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllMovies(): List<RoomMovies> {
    val _sql: String = "SELECT * FROM movieData"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfIDMovie: Int = getColumnIndexOrThrow(_stmt, "IDMovie")
        val _cursorIndexOfNameMovie: Int = getColumnIndexOrThrow(_stmt, "NameMovie")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "Description")
        val _cursorIndexOfIDStudio: Int = getColumnIndexOrThrow(_stmt, "IDStudio")
        val _cursorIndexOfURLimage: Int = getColumnIndexOrThrow(_stmt, "URLimage")
        val _cursorIndexOfTimeOneEP: Int = getColumnIndexOrThrow(_stmt, "TimeOneEP")
        val _cursorIndexOfNummberEP: Int = getColumnIndexOrThrow(_stmt, "NummberEP")
        val _result: MutableList<RoomMovies> = mutableListOf()
        while (_stmt.step()) {
          val _item: RoomMovies
          val _tmpIDMovie: String
          _tmpIDMovie = _stmt.getText(_cursorIndexOfIDMovie)
          val _tmpNameMovie: String
          _tmpNameMovie = _stmt.getText(_cursorIndexOfNameMovie)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          val _tmpIDStudio: String
          _tmpIDStudio = _stmt.getText(_cursorIndexOfIDStudio)
          val _tmpURLimage: String
          _tmpURLimage = _stmt.getText(_cursorIndexOfURLimage)
          val _tmpTimeOneEP: String
          _tmpTimeOneEP = _stmt.getText(_cursorIndexOfTimeOneEP)
          val _tmpNummberEP: String
          _tmpNummberEP = _stmt.getText(_cursorIndexOfNummberEP)
          _item =
              RoomMovies(_tmpIDMovie,_tmpNameMovie,_tmpDescription,_tmpIDStudio,_tmpURLimage,_tmpTimeOneEP,_tmpNummberEP)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getMoviesWithGenres(): List<MovieWithGenres> {
    val _sql: String = "SELECT * FROM movieData"
    return performSuspending(__db, true, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfIDMovie: Int = getColumnIndexOrThrow(_stmt, "IDMovie")
        val _cursorIndexOfNameMovie: Int = getColumnIndexOrThrow(_stmt, "NameMovie")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "Description")
        val _cursorIndexOfIDStudio: Int = getColumnIndexOrThrow(_stmt, "IDStudio")
        val _cursorIndexOfURLimage: Int = getColumnIndexOrThrow(_stmt, "URLimage")
        val _cursorIndexOfTimeOneEP: Int = getColumnIndexOrThrow(_stmt, "TimeOneEP")
        val _cursorIndexOfNummberEP: Int = getColumnIndexOrThrow(_stmt, "NummberEP")
        val _collectionGenres: ArrayMap<String, MutableList<RoomGenre>> =
            ArrayMap<String, MutableList<RoomGenre>>()
        while (_stmt.step()) {
          val _tmpKey: String
          _tmpKey = _stmt.getText(_cursorIndexOfIDMovie)
          if (!_collectionGenres.containsKey(_tmpKey)) {
            _collectionGenres.put(_tmpKey, mutableListOf())
          }
        }
        _stmt.reset()
        __fetchRelationshipmovieGenreAscomExampleFlickfindDATALAYERRoomRoomGenre(_connection,
            _collectionGenres)
        val _result: MutableList<MovieWithGenres> = mutableListOf()
        while (_stmt.step()) {
          val _item: MovieWithGenres
          val _tmpMovie: RoomMovies
          val _tmpIDMovie: String
          _tmpIDMovie = _stmt.getText(_cursorIndexOfIDMovie)
          val _tmpNameMovie: String
          _tmpNameMovie = _stmt.getText(_cursorIndexOfNameMovie)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          val _tmpIDStudio: String
          _tmpIDStudio = _stmt.getText(_cursorIndexOfIDStudio)
          val _tmpURLimage: String
          _tmpURLimage = _stmt.getText(_cursorIndexOfURLimage)
          val _tmpTimeOneEP: String
          _tmpTimeOneEP = _stmt.getText(_cursorIndexOfTimeOneEP)
          val _tmpNummberEP: String
          _tmpNummberEP = _stmt.getText(_cursorIndexOfNummberEP)
          _tmpMovie =
              RoomMovies(_tmpIDMovie,_tmpNameMovie,_tmpDescription,_tmpIDStudio,_tmpURLimage,_tmpTimeOneEP,_tmpNummberEP)
          val _tmpGenresCollection: MutableList<RoomGenre>
          val _tmpKey_1: String
          _tmpKey_1 = _stmt.getText(_cursorIndexOfIDMovie)
          _tmpGenresCollection = _collectionGenres.getValue(_tmpKey_1)
          _item = MovieWithGenres(_tmpMovie,_tmpGenresCollection)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getMovieWithGenresById(movieId: String): MovieWithGenres? {
    val _sql: String = "SELECT * FROM movieData WHERE IDMovie = ?"
    return performSuspending(__db, true, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, movieId)
        val _cursorIndexOfIDMovie: Int = getColumnIndexOrThrow(_stmt, "IDMovie")
        val _cursorIndexOfNameMovie: Int = getColumnIndexOrThrow(_stmt, "NameMovie")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "Description")
        val _cursorIndexOfIDStudio: Int = getColumnIndexOrThrow(_stmt, "IDStudio")
        val _cursorIndexOfURLimage: Int = getColumnIndexOrThrow(_stmt, "URLimage")
        val _cursorIndexOfTimeOneEP: Int = getColumnIndexOrThrow(_stmt, "TimeOneEP")
        val _cursorIndexOfNummberEP: Int = getColumnIndexOrThrow(_stmt, "NummberEP")
        val _collectionGenres: ArrayMap<String, MutableList<RoomGenre>> =
            ArrayMap<String, MutableList<RoomGenre>>()
        while (_stmt.step()) {
          val _tmpKey: String
          _tmpKey = _stmt.getText(_cursorIndexOfIDMovie)
          if (!_collectionGenres.containsKey(_tmpKey)) {
            _collectionGenres.put(_tmpKey, mutableListOf())
          }
        }
        _stmt.reset()
        __fetchRelationshipmovieGenreAscomExampleFlickfindDATALAYERRoomRoomGenre(_connection,
            _collectionGenres)
        val _result: MovieWithGenres?
        if (_stmt.step()) {
          val _tmpMovie: RoomMovies
          val _tmpIDMovie: String
          _tmpIDMovie = _stmt.getText(_cursorIndexOfIDMovie)
          val _tmpNameMovie: String
          _tmpNameMovie = _stmt.getText(_cursorIndexOfNameMovie)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          val _tmpIDStudio: String
          _tmpIDStudio = _stmt.getText(_cursorIndexOfIDStudio)
          val _tmpURLimage: String
          _tmpURLimage = _stmt.getText(_cursorIndexOfURLimage)
          val _tmpTimeOneEP: String
          _tmpTimeOneEP = _stmt.getText(_cursorIndexOfTimeOneEP)
          val _tmpNummberEP: String
          _tmpNummberEP = _stmt.getText(_cursorIndexOfNummberEP)
          _tmpMovie =
              RoomMovies(_tmpIDMovie,_tmpNameMovie,_tmpDescription,_tmpIDStudio,_tmpURLimage,_tmpTimeOneEP,_tmpNummberEP)
          val _tmpGenresCollection: MutableList<RoomGenre>
          val _tmpKey_1: String
          _tmpKey_1 = _stmt.getText(_cursorIndexOfIDMovie)
          _tmpGenresCollection = _collectionGenres.getValue(_tmpKey_1)
          _result = MovieWithGenres(_tmpMovie,_tmpGenresCollection)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getMoviesWithStudios(): List<MovieWithStudios> {
    val _sql: String = "SELECT * FROM movieData"
    return performSuspending(__db, true, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfIDMovie: Int = getColumnIndexOrThrow(_stmt, "IDMovie")
        val _cursorIndexOfNameMovie: Int = getColumnIndexOrThrow(_stmt, "NameMovie")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "Description")
        val _cursorIndexOfIDStudio: Int = getColumnIndexOrThrow(_stmt, "IDStudio")
        val _cursorIndexOfURLimage: Int = getColumnIndexOrThrow(_stmt, "URLimage")
        val _cursorIndexOfTimeOneEP: Int = getColumnIndexOrThrow(_stmt, "TimeOneEP")
        val _cursorIndexOfNummberEP: Int = getColumnIndexOrThrow(_stmt, "NummberEP")
        val _collectionStudios: ArrayMap<String, MutableList<RoomStudio>> =
            ArrayMap<String, MutableList<RoomStudio>>()
        while (_stmt.step()) {
          val _tmpKey: String
          _tmpKey = _stmt.getText(_cursorIndexOfIDMovie)
          if (!_collectionStudios.containsKey(_tmpKey)) {
            _collectionStudios.put(_tmpKey, mutableListOf())
          }
        }
        _stmt.reset()
        __fetchRelationshipstudioDataAscomExampleFlickfindDATALAYERRoomRoomStudio(_connection,
            _collectionStudios)
        val _result: MutableList<MovieWithStudios> = mutableListOf()
        while (_stmt.step()) {
          val _item: MovieWithStudios
          val _tmpMovie: RoomMovies
          val _tmpIDMovie: String
          _tmpIDMovie = _stmt.getText(_cursorIndexOfIDMovie)
          val _tmpNameMovie: String
          _tmpNameMovie = _stmt.getText(_cursorIndexOfNameMovie)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          val _tmpIDStudio: String
          _tmpIDStudio = _stmt.getText(_cursorIndexOfIDStudio)
          val _tmpURLimage: String
          _tmpURLimage = _stmt.getText(_cursorIndexOfURLimage)
          val _tmpTimeOneEP: String
          _tmpTimeOneEP = _stmt.getText(_cursorIndexOfTimeOneEP)
          val _tmpNummberEP: String
          _tmpNummberEP = _stmt.getText(_cursorIndexOfNummberEP)
          _tmpMovie =
              RoomMovies(_tmpIDMovie,_tmpNameMovie,_tmpDescription,_tmpIDStudio,_tmpURLimage,_tmpTimeOneEP,_tmpNummberEP)
          val _tmpStudiosCollection: MutableList<RoomStudio>
          val _tmpKey_1: String
          _tmpKey_1 = _stmt.getText(_cursorIndexOfIDMovie)
          _tmpStudiosCollection = _collectionStudios.getValue(_tmpKey_1)
          _item = MovieWithStudios(_tmpMovie,_tmpStudiosCollection)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getUser(): RoomUser? {
    val _sql: String = "SELECT * FROM userData LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfIDUser: Int = getColumnIndexOrThrow(_stmt, "IDUser")
        val _cursorIndexOfEmail: Int = getColumnIndexOrThrow(_stmt, "Email")
        val _cursorIndexOfPass: Int = getColumnIndexOrThrow(_stmt, "Pass")
        val _cursorIndexOfUserName: Int = getColumnIndexOrThrow(_stmt, "UserName")
        val _cursorIndexOfAvatar: Int = getColumnIndexOrThrow(_stmt, "avatar")
        val _result: RoomUser?
        if (_stmt.step()) {
          val _tmpIDUser: String
          _tmpIDUser = _stmt.getText(_cursorIndexOfIDUser)
          val _tmpEmail: String
          _tmpEmail = _stmt.getText(_cursorIndexOfEmail)
          val _tmpPass: String
          _tmpPass = _stmt.getText(_cursorIndexOfPass)
          val _tmpUserName: String
          _tmpUserName = _stmt.getText(_cursorIndexOfUserName)
          val _tmpAvatar: String
          _tmpAvatar = _stmt.getText(_cursorIndexOfAvatar)
          _result = RoomUser(_tmpIDUser,_tmpEmail,_tmpPass,_tmpUserName,_tmpAvatar)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearAll() {
    val _sql: String = "DELETE FROM movieData"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearUser() {
    val _sql: String = "DELETE FROM userData"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  private
      fun __fetchRelationshipmovieGenreAscomExampleFlickfindDATALAYERRoomRoomGenre(_connection: SQLiteConnection,
      _map: ArrayMap<String, MutableList<RoomGenre>>) {
    val __mapKeySet: Set<String> = _map.keys
    if (__mapKeySet.isEmpty()) {
      return
    }
    if (_map.size > 999) {
      recursiveFetchArrayMap(_map, true) { _tmpMap ->
        __fetchRelationshipmovieGenreAscomExampleFlickfindDATALAYERRoomRoomGenre(_connection,
            _tmpMap)
      }
      return
    }
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT `movieGenre`.`GenreID` AS `GenreID`,`movieGenre`.`GenreName` AS `GenreName`,`movieGenre`.`description` AS `description`,_junction.`IDMovie` FROM `Genre_Movie` AS _junction INNER JOIN `movieGenre` ON (_junction.`GenreID` = `movieGenre`.`GenreID`) WHERE _junction.`IDMovie` IN (")
    val _inputSize: Int = __mapKeySet.size
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    val _stmt: SQLiteStatement = _connection.prepare(_sql)
    var _argIndex: Int = 1
    for (_item: String in __mapKeySet) {
      _stmt.bindText(_argIndex, _item)
      _argIndex++
    }
    try {
      // _junction.IDMovie
      val _itemKeyIndex: Int = 3
      if (_itemKeyIndex == -1) {
        return
      }
      val _cursorIndexOfGenreID: Int = 0
      val _cursorIndexOfGenreName: Int = 1
      val _cursorIndexOfDescription: Int = 2
      while (_stmt.step()) {
        val _tmpKey: String
        _tmpKey = _stmt.getText(_itemKeyIndex)
        val _tmpRelation: MutableList<RoomGenre>? = _map.get(_tmpKey)
        if (_tmpRelation != null) {
          val _item_1: RoomGenre
          val _tmpGenreID: String
          _tmpGenreID = _stmt.getText(_cursorIndexOfGenreID)
          val _tmpGenreName: String
          _tmpGenreName = _stmt.getText(_cursorIndexOfGenreName)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          _item_1 = RoomGenre(_tmpGenreID,_tmpGenreName,_tmpDescription)
          _tmpRelation.add(_item_1)
        }
      }
    } finally {
      _stmt.close()
    }
  }

  private
      fun __fetchRelationshipstudioDataAscomExampleFlickfindDATALAYERRoomRoomStudio(_connection: SQLiteConnection,
      _map: ArrayMap<String, MutableList<RoomStudio>>) {
    val __mapKeySet: Set<String> = _map.keys
    if (__mapKeySet.isEmpty()) {
      return
    }
    if (_map.size > 999) {
      recursiveFetchArrayMap(_map, true) { _tmpMap ->
        __fetchRelationshipstudioDataAscomExampleFlickfindDATALAYERRoomRoomStudio(_connection,
            _tmpMap)
      }
      return
    }
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT `studioData`.`IDStudio` AS `IDStudio`,`studioData`.`StudioName` AS `StudioName`,_junction.`IDMovie` FROM `Studio_Movie` AS _junction INNER JOIN `studioData` ON (_junction.`IDStudio` = `studioData`.`IDStudio`) WHERE _junction.`IDMovie` IN (")
    val _inputSize: Int = __mapKeySet.size
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    val _stmt: SQLiteStatement = _connection.prepare(_sql)
    var _argIndex: Int = 1
    for (_item: String in __mapKeySet) {
      _stmt.bindText(_argIndex, _item)
      _argIndex++
    }
    try {
      // _junction.IDMovie
      val _itemKeyIndex: Int = 2
      if (_itemKeyIndex == -1) {
        return
      }
      val _cursorIndexOfIDStudio: Int = 0
      val _cursorIndexOfStudioName: Int = 1
      while (_stmt.step()) {
        val _tmpKey: String
        _tmpKey = _stmt.getText(_itemKeyIndex)
        val _tmpRelation: MutableList<RoomStudio>? = _map.get(_tmpKey)
        if (_tmpRelation != null) {
          val _item_1: RoomStudio
          val _tmpIDStudio: String
          _tmpIDStudio = _stmt.getText(_cursorIndexOfIDStudio)
          val _tmpStudioName: String
          _tmpStudioName = _stmt.getText(_cursorIndexOfStudioName)
          _item_1 = RoomStudio(_tmpIDStudio,_tmpStudioName)
          _tmpRelation.add(_item_1)
        }
      }
    } finally {
      _stmt.close()
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
