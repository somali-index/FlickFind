package com.example.flickfind.DATALAYER.Room

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.example.flickfind.DATALAYER.DAO.DAOMovie
import com.example.flickfind.DATALAYER.DAO.DAOMovie_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _dAOMovie: Lazy<DAOMovie> = lazy {
    DAOMovie_Impl(this)
  }


  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1,
        "c21e5c5d9283ebe2c40ea5ab0875bc4d", "095001e2248b1ca35543814c648a8a8f") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `movieData` (`IDMovie` TEXT NOT NULL, `NameMovie` TEXT NOT NULL, `Description` TEXT NOT NULL, `IDStudio` TEXT NOT NULL, `URLimage` TEXT NOT NULL, `TimeOneEP` TEXT NOT NULL, `NummberEP` TEXT NOT NULL, PRIMARY KEY(`IDMovie`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `movieGenre` (`GenreID` TEXT NOT NULL, `GenreName` TEXT NOT NULL, `description` TEXT NOT NULL, PRIMARY KEY(`GenreID`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `studioData` (`IDStudio` TEXT NOT NULL, `StudioName` TEXT NOT NULL, PRIMARY KEY(`IDStudio`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `Genre_Movie` (`GenreID` TEXT NOT NULL, `IDMovie` TEXT NOT NULL, PRIMARY KEY(`GenreID`, `IDMovie`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `Studio_Movie` (`IDStudio` TEXT NOT NULL, `IDMovie` TEXT NOT NULL, PRIMARY KEY(`IDStudio`, `IDMovie`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c21e5c5d9283ebe2c40ea5ab0875bc4d')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `movieData`")
        connection.execSQL("DROP TABLE IF EXISTS `movieGenre`")
        connection.execSQL("DROP TABLE IF EXISTS `studioData`")
        connection.execSQL("DROP TABLE IF EXISTS `Genre_Movie`")
        connection.execSQL("DROP TABLE IF EXISTS `Studio_Movie`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsMovieData: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsMovieData.put("IDMovie", TableInfo.Column("IDMovie", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMovieData.put("NameMovie", TableInfo.Column("NameMovie", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMovieData.put("Description", TableInfo.Column("Description", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMovieData.put("IDStudio", TableInfo.Column("IDStudio", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMovieData.put("URLimage", TableInfo.Column("URLimage", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMovieData.put("TimeOneEP", TableInfo.Column("TimeOneEP", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMovieData.put("NummberEP", TableInfo.Column("NummberEP", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysMovieData: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesMovieData: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoMovieData: TableInfo = TableInfo("movieData", _columnsMovieData,
            _foreignKeysMovieData, _indicesMovieData)
        val _existingMovieData: TableInfo = read(connection, "movieData")
        if (!_infoMovieData.equals(_existingMovieData)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |movieData(com.example.flickfind.DATALAYER.Room.RoomMovies).
              | Expected:
              |""".trimMargin() + _infoMovieData + """
              |
              | Found:
              |""".trimMargin() + _existingMovieData)
        }
        val _columnsMovieGenre: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsMovieGenre.put("GenreID", TableInfo.Column("GenreID", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMovieGenre.put("GenreName", TableInfo.Column("GenreName", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMovieGenre.put("description", TableInfo.Column("description", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysMovieGenre: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesMovieGenre: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoMovieGenre: TableInfo = TableInfo("movieGenre", _columnsMovieGenre,
            _foreignKeysMovieGenre, _indicesMovieGenre)
        val _existingMovieGenre: TableInfo = read(connection, "movieGenre")
        if (!_infoMovieGenre.equals(_existingMovieGenre)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |movieGenre(com.example.flickfind.DATALAYER.Room.RoomGenre).
              | Expected:
              |""".trimMargin() + _infoMovieGenre + """
              |
              | Found:
              |""".trimMargin() + _existingMovieGenre)
        }
        val _columnsStudioData: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsStudioData.put("IDStudio", TableInfo.Column("IDStudio", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsStudioData.put("StudioName", TableInfo.Column("StudioName", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysStudioData: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesStudioData: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoStudioData: TableInfo = TableInfo("studioData", _columnsStudioData,
            _foreignKeysStudioData, _indicesStudioData)
        val _existingStudioData: TableInfo = read(connection, "studioData")
        if (!_infoStudioData.equals(_existingStudioData)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |studioData(com.example.flickfind.DATALAYER.Room.RoomStudio).
              | Expected:
              |""".trimMargin() + _infoStudioData + """
              |
              | Found:
              |""".trimMargin() + _existingStudioData)
        }
        val _columnsGenreMovie: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsGenreMovie.put("GenreID", TableInfo.Column("GenreID", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsGenreMovie.put("IDMovie", TableInfo.Column("IDMovie", "TEXT", true, 2, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysGenreMovie: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesGenreMovie: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoGenreMovie: TableInfo = TableInfo("Genre_Movie", _columnsGenreMovie,
            _foreignKeysGenreMovie, _indicesGenreMovie)
        val _existingGenreMovie: TableInfo = read(connection, "Genre_Movie")
        if (!_infoGenreMovie.equals(_existingGenreMovie)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |Genre_Movie(com.example.flickfind.DATALAYER.Room.MovieGenreCrossRef).
              | Expected:
              |""".trimMargin() + _infoGenreMovie + """
              |
              | Found:
              |""".trimMargin() + _existingGenreMovie)
        }
        val _columnsStudioMovie: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsStudioMovie.put("IDStudio", TableInfo.Column("IDStudio", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsStudioMovie.put("IDMovie", TableInfo.Column("IDMovie", "TEXT", true, 2, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysStudioMovie: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesStudioMovie: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoStudioMovie: TableInfo = TableInfo("Studio_Movie", _columnsStudioMovie,
            _foreignKeysStudioMovie, _indicesStudioMovie)
        val _existingStudioMovie: TableInfo = read(connection, "Studio_Movie")
        if (!_infoStudioMovie.equals(_existingStudioMovie)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |Studio_Movie(com.example.flickfind.DATALAYER.Room.MovieStudioCrossRef).
              | Expected:
              |""".trimMargin() + _infoStudioMovie + """
              |
              | Found:
              |""".trimMargin() + _existingStudioMovie)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "movieData", "movieGenre",
        "studioData", "Genre_Movie", "Studio_Movie")
  }

  public override fun clearAllTables() {
    super.performClear(false, "movieData", "movieGenre", "studioData", "Genre_Movie",
        "Studio_Movie")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(DAOMovie::class, DAOMovie_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun movieDao(): DAOMovie = _dAOMovie.value
}
