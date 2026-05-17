package com.example.flickfind.testTinhNang.dataLayerTest.DAO;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import com.example.flickfind.testTinhNang.dataLayerTest.ROOM.UNITYTest;
import java.lang.Class;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class DAOTest_Impl implements DAOTest {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<UNITYTest> __insertAdapterOfUNITYTest;

  public DAOTest_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfUNITYTest = new EntityInsertAdapter<UNITYTest>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `movieData` (`IDMovie`,`NameMovie`,`Description`,`IDStudio`,`URLimage`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final UNITYTest entity) {
        if (entity.getIDMovie() == null) {
          statement.bindNull(1);
        } else {
          statement.bindText(1, entity.getIDMovie());
        }
        if (entity.getNameMovie() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getNameMovie());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getDescription());
        }
        if (entity.getIDStudio() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getIDStudio());
        }
        if (entity.getURLimage() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getURLimage());
        }
      }
    };
  }

  @Override
  public Object insertMovie(final UNITYTest movie, final Continuation<? super Unit> $completion) {
    if (movie == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __insertAdapterOfUNITYTest.insert(_connection, movie);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object insertMovies(final List<UNITYTest> list,
      final Continuation<? super Unit> $completion) {
    if (list == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __insertAdapterOfUNITYTest.insert(_connection, list);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object getAllMovies(final Continuation<? super List<UNITYTest>> $completion) {
    final String _sql = "SELECT * FROM movieData";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfIDMovie = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "IDMovie");
        final int _columnIndexOfNameMovie = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "NameMovie");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Description");
        final int _columnIndexOfIDStudio = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "IDStudio");
        final int _columnIndexOfURLimage = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "URLimage");
        final List<UNITYTest> _result = new ArrayList<UNITYTest>();
        while (_stmt.step()) {
          final UNITYTest _item;
          final String _tmpIDMovie;
          if (_stmt.isNull(_columnIndexOfIDMovie)) {
            _tmpIDMovie = null;
          } else {
            _tmpIDMovie = _stmt.getText(_columnIndexOfIDMovie);
          }
          final String _tmpNameMovie;
          if (_stmt.isNull(_columnIndexOfNameMovie)) {
            _tmpNameMovie = null;
          } else {
            _tmpNameMovie = _stmt.getText(_columnIndexOfNameMovie);
          }
          final String _tmpDescription;
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null;
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription);
          }
          final String _tmpIDStudio;
          if (_stmt.isNull(_columnIndexOfIDStudio)) {
            _tmpIDStudio = null;
          } else {
            _tmpIDStudio = _stmt.getText(_columnIndexOfIDStudio);
          }
          final String _tmpURLimage;
          if (_stmt.isNull(_columnIndexOfURLimage)) {
            _tmpURLimage = null;
          } else {
            _tmpURLimage = _stmt.getText(_columnIndexOfURLimage);
          }
          _item = new UNITYTest(_tmpIDMovie,_tmpNameMovie,_tmpDescription,_tmpIDStudio,_tmpURLimage);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object clearAll(final Continuation<? super Unit> $completion) {
    final String _sql = "DELETE FROM movieData";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
