package com.example.flickfind.testTinhNang.dataLayerTest;

import androidx.annotation.NonNull;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenDelegate;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import com.example.flickfind.testTinhNang.dataLayerTest.DAO.DAOTest;
import com.example.flickfind.testTinhNang.dataLayerTest.DAO.DAOTest_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile DAOTest _dAOTest;

  @Override
  @NonNull
  protected RoomOpenDelegate createOpenDelegate() {
    final RoomOpenDelegate _openDelegate = new RoomOpenDelegate(1, "c1657a3868076dd3fe51f03756178d6b", "3064aa4dab96d03a1399fa4300fc183a") {
      @Override
      public void createAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `movieData` (`IDMovie` TEXT NOT NULL, `NameMovie` TEXT NOT NULL, `Description` TEXT NOT NULL, `IDStudio` TEXT NOT NULL, `URLimage` TEXT NOT NULL, PRIMARY KEY(`IDMovie`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        SQLite.execSQL(connection, "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c1657a3868076dd3fe51f03756178d6b')");
      }

      @Override
      public void dropAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `movieData`");
      }

      @Override
      public void onCreate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      public void onOpen(@NonNull final SQLiteConnection connection) {
        internalInitInvalidationTracker(connection);
      }

      @Override
      public void onPreMigrate(@NonNull final SQLiteConnection connection) {
        DBUtil.dropFtsSyncTriggers(connection);
      }

      @Override
      public void onPostMigrate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      @NonNull
      public RoomOpenDelegate.ValidationResult onValidateSchema(
          @NonNull final SQLiteConnection connection) {
        final Map<String, TableInfo.Column> _columnsMovieData = new HashMap<String, TableInfo.Column>(5);
        _columnsMovieData.put("IDMovie", new TableInfo.Column("IDMovie", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovieData.put("NameMovie", new TableInfo.Column("NameMovie", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovieData.put("Description", new TableInfo.Column("Description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovieData.put("IDStudio", new TableInfo.Column("IDStudio", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovieData.put("URLimage", new TableInfo.Column("URLimage", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysMovieData = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesMovieData = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMovieData = new TableInfo("movieData", _columnsMovieData, _foreignKeysMovieData, _indicesMovieData);
        final TableInfo _existingMovieData = TableInfo.read(connection, "movieData");
        if (!_infoMovieData.equals(_existingMovieData)) {
          return new RoomOpenDelegate.ValidationResult(false, "movieData(com.example.flickfind.testTinhNang.dataLayerTest.ROOM.UNITYTest).\n"
                  + " Expected:\n" + _infoMovieData + "\n"
                  + " Found:\n" + _existingMovieData);
        }
        return new RoomOpenDelegate.ValidationResult(true, null);
      }
    };
    return _openDelegate;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final Map<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final Map<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "movieData");
  }

  @Override
  public void clearAllTables() {
    super.performClear(false, "movieData");
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final Map<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(DAOTest.class, DAOTest_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final Set<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public DAOTest movieDao() {
    if (_dAOTest != null) {
      return _dAOTest;
    } else {
      synchronized(this) {
        if(_dAOTest == null) {
          _dAOTest = new DAOTest_Impl(this);
        }
        return _dAOTest;
      }
    }
  }
}
